# Ngirwi Medical - Deployment Guide

## Prerequisites

### On Your Local Machine
- Java 11
- Maven 3.6+
- Node.js 18 LTS
- npm

### On VPS
- Ubuntu 20.04+ or Debian 11+
- Root access
- Open ports: 22 (SSH), 80 (HTTP), 443 (HTTPS)

---

## Step 1: Setup SendGrid for Email

### Create SendGrid Account

1. Go to [https://sendgrid.com](https://sendgrid.com)
2. Click "Start for Free" (100 emails/day free)
3. Complete registration and email verification

### Generate API Key

1. Go to **Settings** → **API Keys**
2. Click **Create API Key**
3. Name it: `ngirwi-medical-production`
4. Select **Full Access** or **Restricted Access** with:
   - Mail Send: Full Access
5. Click **Create & View**
6. **COPY THE API KEY NOW** (you won't see it again!)

### Verify Sender Identity

1. Go to **Settings** → **Sender Authentication**
2. Choose **Single Sender Verification** (easiest for testing)
3. Add your email address
4. Verify via the email link

---

## Step 2: Prepare VPS

### Connect to VPS

```bash
ssh root@45.130.229.144
```

### IMPORTANT: Change Password First!

```bash
passwd
```

### Upload and Run Install Script

From your local machine:

```bash
scp deploy/install.sh root@45.130.229.144:/root/
```

On VPS:

```bash
chmod +x /root/install.sh
bash /root/install.sh
```

---

## Step 3: Configure Credentials

### Set PostgreSQL Password

```bash
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'YourSecurePassword123!';"
```

### Edit Environment File

```bash
nano /opt/ngirwi/config/.env
```

Fill in:

```env
DB_PASSWORD=YourSecurePassword123!
SENDGRID_API_KEY=SG.xxxxxxxxxxxxxxxxxxxxxx
JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=YourVeryLongRandomStringHere
```

Generate JWT secret:

```bash
openssl rand -base64 64
```

---

## Step 4: Build Application (Local Machine)

```bash
cd /home/ibrahim/Bureau/Ngirwi/Ngirwi
chmod +x deploy/build.sh
./deploy/build.sh
```

---

## Step 5: Deploy to VPS

### Upload Package

```bash
scp deploy/dist/ngirwi-medical-deploy.tar.gz root@45.130.229.144:/opt/ngirwi/
```

### Extract on VPS

```bash
cd /opt/ngirwi
tar -xzvf ngirwi-medical-deploy.tar.gz
chown -R ngirwi:ngirwi /opt/ngirwi
```

---

## Step 6: Start Application

```bash
# Start backend
systemctl start ngirwi-backend
systemctl enable ngirwi-backend

# Check status
systemctl status ngirwi-backend

# View logs
tail -f /opt/ngirwi/logs/backend.log
```

---

## Step 7: Access Application

Your application will be available at:

- **HTTPS**: `https://45-130-229-144.nip.io`
- **HTTP fallback**: `http://45.130.229.144`

---

## Troubleshooting

### Check Backend Logs

```bash
tail -100 /opt/ngirwi/logs/backend.log
tail -100 /opt/ngirwi/logs/backend-error.log
```

### Check Nginx Logs

```bash
tail -100 /var/log/nginx/error.log
```

### Restart Services

```bash
systemctl restart ngirwi-backend
systemctl restart nginx
```

### Database Connection Issues

```bash
# Test PostgreSQL connection
sudo -u postgres psql -d Ngirwi -c "SELECT 1;"

# Check PostgreSQL is running
systemctl status postgresql
```

### Port Issues

```bash
# Check what's using port 8080
netstat -tlnp | grep 8080

# Check firewall
ufw status
```

---

## Security Checklist

- [ ] Changed SSH root password
- [ ] Set up SSH key authentication (optional but recommended)
- [ ] All secrets in .env file are unique and strong
- [ ] .env file has restricted permissions (chmod 600)
- [ ] Firewall is enabled
- [ ] SSL certificate is active

---

## Useful Commands

```bash
# Application management
systemctl start ngirwi-backend
systemctl stop ngirwi-backend
systemctl restart ngirwi-backend
systemctl status ngirwi-backend

# View real-time logs
journalctl -u ngirwi-backend -f

# Nginx management
nginx -t                    # Test config
systemctl reload nginx      # Reload config
systemctl restart nginx     # Full restart

# SSL certificate renewal (automatic, but manual if needed)
certbot renew

# Database backup
pg_dump -U postgres Ngirwi > backup_$(date +%Y%m%d).sql
```

