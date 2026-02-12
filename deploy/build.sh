#!/bin/bash
# =============================================================================
# NGIRWI MEDICAL - Build Script (Run on local machine)
# =============================================================================
# This script builds the application for production deployment
# Run from the project root directory
# =============================================================================

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
BUILD_OUTPUT="$SCRIPT_DIR/dist"

log_info "=== NGIRWI MEDICAL BUILD ==="
log_info "Project root: $PROJECT_ROOT"
log_info "Build output: $BUILD_OUTPUT"
echo ""

# Create output directory
rm -rf "$BUILD_OUTPUT"
mkdir -p "$BUILD_OUTPUT"/{backend,frontend}

# =============================================================================
# BUILD BACKEND
# =============================================================================
log_info "Building Backend (Spring Boot)..."

cd "$PROJECT_ROOT/ngirwi-medical-backend-master"

# Clean and build with production profile
./mvnw clean package -Pprod -DskipTests

# Copy JAR to output
cp target/*.jar "$BUILD_OUTPUT/backend/app.jar"

log_info "Backend JAR created: $BUILD_OUTPUT/backend/app.jar"

# =============================================================================
# BUILD FRONTEND
# =============================================================================
log_info "Building Frontend (React)..."

cd "$PROJECT_ROOT/ngirwi-medical-react-master"

# Install dependencies
npm ci --legacy-peer-deps

# Build for production
npm run build

# Copy build output
cp -r target/classes/static/* "$BUILD_OUTPUT/frontend/"

log_info "Frontend built: $BUILD_OUTPUT/frontend/"

# =============================================================================
# CREATE DEPLOYMENT PACKAGE
# =============================================================================
log_info "Creating deployment package..."

cd "$BUILD_OUTPUT"

# Create tarball
tar -czvf ngirwi-medical-deploy.tar.gz backend frontend

log_info "Deployment package created: $BUILD_OUTPUT/ngirwi-medical-deploy.tar.gz"

# =============================================================================
# SUMMARY
# =============================================================================
echo ""
echo "============================================================================="
echo -e "${GREEN}BUILD COMPLETE!${NC}"
echo "============================================================================="
echo ""
echo "Files ready for deployment:"
echo "  - $BUILD_OUTPUT/ngirwi-medical-deploy.tar.gz"
echo ""
echo "To deploy to VPS:"
echo "  1. Copy install.sh to VPS and run it first"
echo "  2. Then upload the deployment package:"
echo ""
echo "     scp $BUILD_OUTPUT/ngirwi-medical-deploy.tar.gz root@YOUR_VPS_IP:/opt/ngirwi/"
echo ""
echo "  3. On VPS, extract and set permissions:"
echo "     cd /opt/ngirwi && tar -xzvf ngirwi-medical-deploy.tar.gz"
echo "     chown -R ngirwi:ngirwi /opt/ngirwi"
echo ""
echo "  4. Start the service:"
echo "     systemctl start ngirwi-backend"
echo ""
echo "============================================================================="

