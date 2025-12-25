import React, { useState } from 'react';
import { HiOutlineClipboard, HiOutlineOfficeBuilding } from 'react-icons/hi';
import { MdOutlinePeopleOutline } from 'react-icons/md';
import { CgCalendarDates } from 'react-icons/cg';
import { FaHome } from 'react-icons/fa';
import { FiLogOut } from 'react-icons/fi';
import { Link } from 'react-router-dom';
import { BrandIcon } from '../header/header-components';
import '../header/header.scss';
import { Nav } from 'reactstrap';
import { RiAdminLine, RiFilePaperLine } from 'react-icons/ri';
import { AUTHORITIES } from 'app/config/constants';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface ISideBarProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}

const SideBar = (props: ISideBarProps) => {
  const account = useAppSelector(state => state.authentication.account);
  const isDoctor = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.DOCTOR]));
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev" style={{ position: 'fixed' }}>
        <a href="">Development</a>
      </div>
    ) : null;

  const [isShown, setIsShown] = useState(true);

  function changeColor(e) {
    e.target.style.color = '#000000';
  }
  function setColor(e) {
    e.target.style.color = '#3C5681';
  }

  return (
    <Nav>
      {renderDevRibbon()}
      <div
        style={{
          backgroundColor: '#FFFFFF',
          height: '100vh',
          width: '14.06vw',
          position: 'fixed',
          color: '#54BFD0',
        }}
      >
        <div
          style={{
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Link
            to="/"
            style={{
              display: 'flex',
              flexDirection: 'row',
              marginBottom: '20%',
              alignItems: 'center',
              marginRight: '10%',
              justifyContent: 'center',
              textDecoration: 'none',
            }}
          >
            <BrandIcon style={{ width: '5vw' }} />
            <span
              style={{
                fontFamily: 'Mulish',
                fontWeight: '900',
                color: '#141414',
                position: 'relative',
                paddingRight: '10%',
              }}
            >
              NGIRWI MEDICAL
            </span>
          </Link>
          <div style={{ display: 'flex', flexDirection: 'column', width: '14.06vw', justifyContent: 'space-between' }}>
            <Link
              to="/"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(FaHome, { size: '15' })}</div>
              <h6
                onClick={changeColor}
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Accueil
              </h6>
            </Link>
            <Link
              to="/hospitalisation?page=1&sort=id,asc"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(CgCalendarDates, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Hospitalisations
              </h6>
            </Link>
            <Link
              to="patient?page=1&sort=id,asc"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(MdOutlinePeopleOutline, { size: '15' })}</div>
              <h6
                onMouseEnter={changeColor}
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Patients
              </h6>
            </Link>
            <Link
              to="/hospital?page=1&sort=id,asc"
              hidden={!isAdmin}
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(HiOutlineOfficeBuilding, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Hôpitaux
              </h6>
            </Link>
            <Link
              to="/consultation?page=1&sort=id,asc"
              hidden={!isDoctor}
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(HiOutlineClipboard, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Consultations
              </h6>
            </Link>
            <Link
              hidden={!isDoctor}
              to="/prescription?page=1&sort=id,asc"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(RiFilePaperLine, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Ordonnances
              </h6>
            </Link>
            <Link
              to="bill?page=1&sort=id,asc"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(CgCalendarDates, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Factures
              </h6>
            </Link>
            <Link
              hidden={!isAdmin}
              to="/admin/user-management/"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
              }}
            >
              <div>{React.createElement(RiAdminLine, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Administration
              </h6>
            </Link>

            <Link
              to="/logoutDialog"
              style={{
                paddingLeft: '1.5vw',
                marginBottom: '1vh',
                width: '14.06vw',
                color: '#54BFD0',
                display: 'flex',
                alignItems: 'flex-start',
                fontSize: '5px',
                gap: '5%',
                fontFamily: 'Mulish',
                textDecoration: 'none',
                marginTop: '30vh',
              }}
            >
              <div>{React.createElement(FiLogOut, { size: '15' })}</div>
              <h6
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                style={{
                  fontFamily: 'Mulish',
                  color: '#3C5681',
                }}
              >
                Déconnexion
              </h6>
            </Link>
          </div>

          <div
            style={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              height: '30%',
            }}
          >
            <BrandIcon
              style={{
                width: '8vw',
                height: '60%',
              }}
            />
            <span
              style={{
                fontFamily: 'Mulish',
                fontWeight: '900',
                fontSize: '10px',
                color: 'silver',
              }}
            >
              Copyright @2024
            </span>
          </div>
        </div>
      </div>
    </Nav>
  );
};
export default SideBar;
