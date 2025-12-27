import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, Modal, ModalBody, ModalFooter, ModalHeader, Table } from 'reactstrap';
import { TextFormat, getSortState, ValidatedField } from 'react-jhipster';
import { RiUserAddLine } from 'react-icons/ri';
import { BiTrash } from 'react-icons/bi';

import { APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesBis } from './patient.reducer';
import Header from 'app/shared/layout/header/header';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const Patient = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const account = useAppSelector(state => state.authentication.account);
  const patientList = useAppSelector(state => state.patient.entities) || [];
  const loading = useAppSelector(state => state.patient.loading);
  const isDoctor = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.DOCTOR]));
  // const hospital = useAppSelector(state => state.hospital.entity);
  // const [modal, setModal] = useState(false);
  // const toggleModal = () => setModal(!modal);

  // filter
  const [search, setSearch] = useState('');
  const [criteria, setCriteria] = useState(' ');

  const getAllEntities = () => {
    dispatch(
      getEntitiesBis({
        // id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  // filtering table
  const handleSearch = event => {
    setSearch(event.target.value);
  };

  let filter = null;
  if (search !== '') {
    switch (criteria) {
      case 'lastName':
        filter = patientList.filter(patient => {
          return patient.lastName.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'firstName':
        filter = patientList.filter(patient => {
          return patient.firstName.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'birthday':
        filter = patientList.filter(patient => {
          return patient.birthday.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'cni':
        filter = patientList.filter(patient => {
          return patient.cni.toLowerCase().includes(search.toLowerCase());
        });
        break;
      default:
        filter = null;
    }
  }

  const filteredPatients = filter !== null ? filter : patientList;

  const [query, setQuery] = useState('');

  // const generatePdf = () => {
  //   // Generate PDF content
  //   setModal(true); // Open modal after generating PDF
  // };

  return (
    <>
      <div
        style={{
          paddingLeft: '16vw',
          paddingTop: '1%',
          fontFamily: 'Mulish',
          fontWeight: '900',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <Header pageName="Gestion patients" />

        <div
          style={{
            display: 'flex',
            flexDirection: 'column',
            position: 'fixed',
            top: '15.5vh',
          }}
        >
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(3, 3fr)',
              alignItems: 'center',
              columnGap: '5%',
              width: '75vw',
              marginLeft: '5%',
            }}
          >
            <Link
              to="/patient/new/"
              style={{
                display: 'flex',
                textDecoration: 'none',
                textAlign: 'center',
                color: '#56B5C5',
                minWidth: '15vw',
                minHeight: '15vw',
                borderRadius: '50%',
                backgroundColor: '#CBDCF7',
                fontSize: '18px',
                paddingTop: '20%',
                justifyContent: 'center',
              }}
            >
              <span style={{ display: 'block', width: '90%', wordBreak: 'break-word' }}>
                {React.createElement(RiUserAddLine, { size: '24' })} Enregistrer nouveau patient
              </span>
            </Link>
            <Card
              style={{
                height: '6.28vh',
                width: '33.38vw',
                borderRadius: '20px',
                backgroundColor: '#11485C',
                textAlign: 'center',
                color: 'white',
                marginBottom: '4vw',
                boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
              }}
            >
              <span style={{ marginTop: '1.5%' }}>Liste des patients enregistrés</span>
            </Card>
            <Link
              to={isDoctor ? '/consultation/new/' : '#'}
              style={{
                display: 'flex',
                textDecoration: 'none',
                textAlign: 'center',
                color: '#56B5C5',
                minWidth: '15vw',
                minHeight: '15vw',
                borderRadius: '50%',
                backgroundColor: '#CBDCF7',
                fontSize: '18px',
                paddingTop: '20%',
                justifyContent: 'center',
              }}
            >
              <span style={{ display: 'block', width: '90%', wordBreak: 'break-word' }}>
                {React.createElement(RiUserAddLine, { size: '24' })} Enregistrer nouvelle consultation
              </span>
            </Link>
          </div>

          <Card
            style={{
              width: '83vw',
              height: '70vh',
              backgroundColor: 'white',
              position: 'fixed',
              top: '32vh',
              marginRight: '1%',
              borderRadius: '15px 15px 0px 0px',
              boxShadow: '0px 2px 12px 4px rgba(138, 161, 203, 0.23)',
            }}
          >
            <div style={{ display: 'flex', flexDirection: 'row', marginTop: '1%' }}>
              <span style={{ color: '#141414', fontSize: '20px', marginLeft: '3%', marginBottom: '1%', width: '45vw' }}>
                Patients enregistrés
              </span>

              <div
                style={{
                  display: 'flex',
                  flexDirection: 'row',
                  justifyContent: 'flex-end',
                  marginBottom: '3vh',
                  marginRight: '2vw',
                  gap: '1vw',
                }}
              >
                <ValidatedField
                  style={{ borderRadius: '12px', width: '17vw' }}
                  id="criteria"
                  name="criteria"
                  type="select"
                  onChange={e => setCriteria(e.target.value)}
                >
                  {/* <select name="criteria" > */}
                  <option value=" ">Critère de recherche</option>
                  <option value="lastName">Nom</option>
                  <option value="firstName">Prénom</option>
                  <option value="birthday">Date de naissance</option>
                  <option value="cni">Numéro de carte d&apos;identité</option>
                  {/* </select> */}
                </ValidatedField>
                <ValidatedField
                  style={{ borderRadius: '12px', width: '17vw' }}
                  placeholder="Barre de recherche"
                  id="search"
                  name="search"
                  type={criteria === 'birthday' ? 'date' : 'text'}
                  onChange={handleSearch}
                />
                {/* <input type="text" id="search" name="search" placeholder="Barre de recherche" onChange={handleSearch} />  */}
              </div>
            </div>
            {filteredPatients.length > 0 ?  (
              <Table responsive style={{ borderCollapse: 'separate', borderSpacing: '0 15px' }}>
                <thead
                  style={{
                    position: 'sticky',
                    top: '0',
                  }}
                >
                  <tr>
                    <th
                      style={{
                        position: 'sticky',
                        top: '0',
                        width: '4%',
                        backgroundColor: 'white',
                      }}
                    ></th>
                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                      className="hand"
                      onClick={sort('id')}
                    >
                      ID <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                      className="hand"
                      onClick={sort('firstName')}
                    >
                      Prénom <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                      className="hand"
                      onClick={sort('lastName')}
                    >
                      Nom <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                      className="hand"
                      onClick={sort('birthday')}
                    >
                      Date de naissance <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                      className="hand"
                      onClick={sort('cni')}
                    >
                      Cni <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>

                    <th
                      style={{
                        textAlign: 'center',
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        width: '16%',
                        backgroundColor: 'white',
                      }}
                    >
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody
                  style={{
                    backgroundColor: '#F6FAFF',
                    border: '1px solid #F6FAFF',
                    borderRadius: '15px 15px 0px 15px',
                    fontSize: '15px',
                    textAlign: 'center',
                    borderBottom: '50px solid white',
                    backgroundImage: 'url(content/images/NgirwiLogo.png)',
                    backgroundRepeat: 'no-repeat',
                    backgroundAttachment: 'fixed',
                    backgroundPosition: '60% 165%',
                  }}
                >
                  {filter === null
                    ? patientList.map((patient, i) => (
                        <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                          <td>
                            <Button
                              tag={Link}
                              to={`/patient/${patient.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                              data-cy="entityDeleteButton"
                              style={{ color: 'red', backgroundColor: '#F6FAFF', borderColor: '#F6FAFF' }}
                            >
                              {React.createElement(BiTrash, { size: '15' })}
                            </Button>
                          </td>

                          <td>
                            <Button
                              tag={Link}
                              to={`/patient/${patient.id}`}
                              color="link"
                              style={{ color: '#91A8CD', textDecoration: 'none' }}
                            >
                              {patient.id}
                            </Button>
                          </td>
                          <td style={{ wordBreak: 'break-all', textTransform: 'capitalize' }}>{patient.firstName}</td>
                          <td>{patient.lastName.toUpperCase()}</td>
                          <td>
                            {patient.birthday ? <TextFormat type="date" value={patient.birthday} format={APP_LOCAL_DATE_FORMAT} /> : null}
                          </td>
                          <td>{patient.cni}</td>
                          <td className="text-end">
                            <div
                              style={{
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '1px',
                                fontSize: '9px',
                              }}
                            >
                              <Button
                                tag={Link}
                                to={`/patient/${patient.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                color="primary"
                                size="sm"
                                data-cy="entityEditButton"
                              >
                                <FontAwesomeIcon icon="pencil" /> <span className="d-none d-md-inline">Mettre à jour</span>
                              </Button>
                              <Button tag={Link} to={`/patient/${patient.id}`} color="dark" size="sm" data-cy="entityDetailsButton">
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir détails</span>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))
                    : filter.map((patient, i) => (
                        <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                          <td>
                            <Button
                              tag={Link}
                              to={`/patient/${patient.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                              data-cy="entityDeleteButton"
                              style={{ color: 'red', backgroundColor: '#F6FAFF', borderColor: '#F6FAFF' }}
                            >
                              {React.createElement(BiTrash, { size: '15' })}
                            </Button>
                          </td>
                          <td>
                            <Button tag={Link} to={`/patient/${patient.id}`} color="link" size="sm">
                              {patient.id}
                            </Button>
                          </td>
                          <td style={{ wordBreak: 'break-all', textTransform: 'capitalize' }}>{patient.firstName}</td>
                          <td>{patient.lastName.toUpperCase()}</td>
                          <td>
                            {patient.birthday ? <TextFormat type="date" value={patient.birthday} format={APP_LOCAL_DATE_FORMAT} /> : null}
                          </td>

                          <td>{patient.cni}</td>
                          <td className="text-end">
                            <div
                              style={{
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '1px',
                                fontSize: '9px',
                              }}
                            >
                              <Button
                                tag={Link}
                                to={`/patient/${patient.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                color="primary"
                                size="sm"
                                data-cy="entityEditButton"
                              >
                                <FontAwesomeIcon icon="pencil" /> <span className="d-none d-md-inline">Mettre à jour</span>
                              </Button>
                              <Button tag={Link} to={`/patient/${patient.id}`} color="dark" size="sm" data-cy="entityDetailsButton">
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir détails</span>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                </tbody>
              </Table>
            ) : (
              !loading && <div className="alert alert-warning">Aucun patient enregistré</div>
            )}
          </Card>
        </div>
      </div>
    </>
  );
};

export default Patient;
