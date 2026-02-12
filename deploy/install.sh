#!/bin/bash
# =============================================================================
# NGIRWI MEDICAL - VPS Deployment Script
# =============================================================================
# Run this script on your VPS as root
# Usage: bash install.sh
# =============================================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# =============================================================================
# CONFIGURATION - MODIFY THESE VALUES
# =============================================================================
APP_NAME="ngirwi-medical"
APP_USER="ngirwi"
APP_DIR="/opt/ngirwi"
DB_NAME="Ngirwi"
DB_USER="postgres"

# Get server IP for nip.io domain
SERVER_IP=$(curl -s ifconfig.me)
DOMAIN="${SERVER_IP//./-}.nip.io"

log_info "=== NGIRWI MEDICAL DEPLOYMENT ==="
log_info "Server IP: $SERVER_IP"
log_info "Domain: $DOMAIN"
echo ""

# =============================================================================
# STEP 1: System Update & Prerequisites
# =============================================================================
log_info "Step 1: Installing system prerequisites..."

apt update && apt upgrade -y
apt install -y \
    openjdk-11-jdk \
    postgresql \
    postgresql-contrib \
    nginx \
    certbot \
    python3-certbot-nginx \
    curl \
    git \
    unzip \
    ufw

# Install Node.js 18
if ! command -v node &> /dev/null; then
    log_info "Installing Node.js 18..."
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
    apt install -y nodejs
fi

log_info "Java version: $(java -version 2>&1 | head -n1)"
log_info "Node version: $(node -v)"
log_info "npm version: $(npm -v)"

# =============================================================================
# STEP 2: Create Application User
# =============================================================================
log_info "Step 2: Creating application user..."

if ! id "$APP_USER" &>/dev/null; then
    useradd -r -m -d "$APP_DIR" -s /bin/bash "$APP_USER"
    log_info "Created user: $APP_USER"
else
    log_info "User $APP_USER already exists"
fi

mkdir -p "$APP_DIR"/{backend,frontend,logs,config}
chown -R "$APP_USER:$APP_USER" "$APP_DIR"

# =============================================================================
# STEP 3: Configure PostgreSQL
# =============================================================================
log_info "Step 3: Configuring PostgreSQL..."

systemctl start postgresql
systemctl enable postgresql

# Create database if not exists
sudo -u postgres psql -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 || \
    sudo -u postgres psql -c "CREATE DATABASE \"$DB_NAME\";"

log_info "Database '$DB_NAME' is ready"
log_warn "Remember to set the postgres password with: sudo -u postgres psql -c \"ALTER USER postgres PASSWORD 'your_password';\""

# =============================================================================
# STEP 4: Configure Firewall
# =============================================================================
log_info "Step 4: Configuring firewall..."

ufw --force reset
ufw default deny incoming
ufw default allow outgoing
ufw allow ssh
ufw allow http
ufw allow https
ufw --force enable

log_info "Firewall configured (SSH, HTTP, HTTPS allowed)"

# =============================================================================
# STEP 5: Create Directory Structure
# =============================================================================
log_info "Step 5: Creating directory structure..."

cat > "$APP_DIR/config/application-prod-override.yml" << 'EOF'
# Production override configuration
# This file overrides application-prod.yml settings

spring:
  datasource:
    password: ${DB_PASSWORD}
  mail:
    password: ${SENDGRID_API_KEY}

jhipster:
  mail:
    from: noreply@ngirwi-medical.com
    base-url: https://${DOMAIN}
EOF

# =============================================================================
# STEP 6: Create Environment File
# =============================================================================
log_info "Step 6: Creating environment file..."

cat > "$APP_DIR/config/.env" << EOF
# =============================================================================
# NGIRWI MEDICAL - Environment Variables
# =============================================================================
# IMPORTANT: Fill in these values before starting the application!

# Database Configuration
DB_PASSWORD=CHANGE_ME_DB_PASSWORD

# SendGrid Email Configuration
# Get your API key from: https://app.sendgrid.com/settings/api_keys
SENDGRID_API_KEY=CHANGE_ME_SENDGRID_API_KEY

# JWT Secret (generate a strong random string)
# You can generate one with: openssl rand -base64 64
JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=CHANGE_ME_JWT_SECRET

# Domain (auto-detected)
DOMAIN=$DOMAIN

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m
EOF

chmod 600 "$APP_DIR/config/.env"
chown "$APP_USER:$APP_USER" "$APP_DIR/config/.env"

log_warn "IMPORTANT: Edit $APP_DIR/config/.env and fill in your credentials!"

# =============================================================================
# STEP 7: Create Systemd Service
# =============================================================================
log_info "Step 7: Creating systemd service..."

cat > /etc/systemd/system/ngirwi-backend.service << EOF
[Unit]
Description=Ngirwi Medical Backend
After=network.target postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=$APP_USER
Group=$APP_USER
WorkingDirectory=$APP_DIR/backend
EnvironmentFile=$APP_DIR/config/.env
ExecStart=/usr/bin/java \$JAVA_OPTS -jar $APP_DIR/backend/app.jar --spring.profiles.active=prod
ExecStop=/bin/kill -SIGTERM \$MAINPID
Restart=on-failure
RestartSec=10
StandardOutput=append:$APP_DIR/logs/backend.log
StandardError=append:$APP_DIR/logs/backend-error.log

# Security hardening
NoNewPrivileges=true
PrivateTmp=true

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload

# =============================================================================
# STEP 8: Configure Nginx
# =============================================================================
log_info "Step 8: Configuring Nginx..."

cat > /etc/nginx/sites-available/ngirwi-medical << EOF
server {
    listen 80;
    server_name $DOMAIN $SERVER_IP;

    # Frontend static files
    root $APP_DIR/frontend;
    index index.html;

    # Gzip compression
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;

    # API proxy to backend
    location /api {
        proxy_pass http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_read_timeout 90;
        proxy_connect_timeout 90;
    }

    # Management endpoints (health check, etc.)
    location /management {
        proxy_pass http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    # Swagger/OpenAPI
    location /v3/api-docs {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host \$host;
    }

    # WebSocket support (if needed)
    location /websocket {
        proxy_pass http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host \$host;
    }

    # SPA routing - serve index.html for all non-file requests
    location / {
        try_files \$uri \$uri/ /index.html;
    }

    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
EOF

# Enable site
ln -sf /etc/nginx/sites-available/ngirwi-medical /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default

# Test and reload nginx
nginx -t && systemctl reload nginx

log_info "Nginx configured for $DOMAIN"

# =============================================================================
# STEP 9: SSL Certificate (Let's Encrypt)
# =============================================================================
log_info "Step 9: Setting up SSL certificate..."

# Only attempt SSL if using nip.io domain
if [[ "$DOMAIN" == *"nip.io"* ]]; then
    log_info "Attempting to get Let's Encrypt certificate for $DOMAIN..."
    certbot --nginx -d "$DOMAIN" --non-interactive --agree-tos --email admin@ngirwi.com --redirect || {
        log_warn "SSL certificate failed. You can retry later with:"
        log_warn "certbot --nginx -d $DOMAIN"
    }
else
    log_warn "Skipping SSL - not using nip.io domain"
fi

# =============================================================================
# SUMMARY
# =============================================================================
echo ""
echo "============================================================================="
echo -e "${GREEN}INSTALLATION COMPLETE!${NC}"
echo "============================================================================="
echo ""
echo "Domain: https://$DOMAIN (or http://$SERVER_IP)"
echo ""
echo "NEXT STEPS:"
echo "1. Edit environment file: nano $APP_DIR/config/.env"
echo "   - Set DB_PASSWORD"
echo "   - Set SENDGRID_API_KEY"
echo "   - Set JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET"
echo ""
echo "2. Set PostgreSQL password:"
echo "   sudo -u postgres psql -c \"ALTER USER postgres PASSWORD 'your_password';\""
echo ""
echo "3. Upload application files:"
echo "   - Backend JAR: $APP_DIR/backend/app.jar"
echo "   - Frontend:    $APP_DIR/frontend/"
echo ""
echo "4. Start the backend:"
echo "   systemctl start ngirwi-backend"
echo "   systemctl enable ngirwi-backend"
echo ""
echo "5. Check logs:"
echo "   tail -f $APP_DIR/logs/backend.log"
echo ""
echo "============================================================================="

