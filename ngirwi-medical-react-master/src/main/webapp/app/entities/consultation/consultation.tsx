import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { TextFormat, getSortState, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesBis as getEntities } from './consultation.reducer';
import { getEntitiesBis as getPatients } from '../patient/patient.reducer';
import { RiUserAddLine } from 'react-icons/ri';
import { BiTrash } from 'react-icons/bi';
import Header from 'app/shared/layout/header/header';
import { convertDateTimeFromServerToHours } from 'app/shared/util/date-utils';

export const Consultation = () => {
  const dispatch = useAppDispatch();

  const patientList = useAppSelector(state => state.patient.entities);

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const consultationList = useAppSelector(state => state.consultation.entities);
  // const patients = useAppSelector(state => state.patient.entities);
  const loading = useAppSelector(state => state.consultation.loading);
  const totalItems = useAppSelector(state => state.consultation.totalItems);
  const account = useAppSelector(state => state.authentication.account);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0,
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
    dispatch(
      getPatients({
        id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0,
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

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  // filtering table
  const handleSearch = event => {
    setSearch(event.target.value);
  };
  // filter
  const [search, setSearch] = useState('');
  const [criteria, setCriteria] = useState(' ');

  let filter = null;
  if (search !== '') {
    switch (criteria) {
      case 'patient.lastName':
        filter = consultationList.filter(consultation => {
          return consultation.patient.lastName.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'patient.firstName':
        filter = consultationList.filter(consultation => {
          return consultation.patient.firstName.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'dateTime':
        filter = consultationList.filter(consultation => {
          return consultation.dateTime.toLowerCase().includes(search.toLowerCase());
        });
        break;

      default:
        filter = null;
    }
  }
  const [query, setQuery] = useState('');

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
        <Header pageName="Gestion consultations" />

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
              <span style={{ marginTop: '1.5%' }}>Liste des consultations enregistrées</span>
            </Card>
            <Link
              to="/consultation/new/"
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
                Consultations enregistrées
              </span>
              <div
                style={{
                  display: 'flex',
                  flexDirection: 'row',
                  justifyContent: 'flex-end',
                  marginBottom: '5vh',
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
                  <option value="patient.lastName">Nom</option>
                  <option value="patient.firstName">Prénom</option>
                  <option value="dateTime">Date de consultation</option>
                  {/* </select> */}
                </ValidatedField>
                <ValidatedField
                  style={{ borderRadius: '12px', width: '17vw' }}
                  placeholder="Barre de recherche"
                  id="search"
                  name="search"
                  type={criteria === 'dateTime' ? 'date' : 'text'}
                  onChange={handleSearch}
                />
                {/* <input type="text" id="search" name="search" placeholder="Barre de recherche" onChange={handleSearch} />  */}
              </div>
            </div>
            {consultationList && consultationList.length > 0 ? (
              <Table style={{ borderCollapse: 'separate', borderSpacing: '0 15px' }} responsive>
                <thead>
                  <tr>
                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '1%',
                      }}
                    ></th>
                    {/* <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '10%',
                        textAlign: 'center',
                      }}
                      className="hand"
                      onClick={sort('id')}
                    >
                      ID <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th> */}
                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '17%',
                        textAlign: 'center',
                      }}
                      className="hand"
                      onClick={sort('patient.lastName')}
                    >
                      Nom <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '17%',
                        textAlign: 'center',
                      }}
                      className="hand"
                      onClick={sort('patient.firstName')}
                    >
                      Prénom <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '10%',
                        textAlign: 'center',
                      }}
                      className="hand"
                      onClick={sort('dateTime')}
                    >
                      Date <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>
                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '11%',
                        textAlign: 'center',
                      }}
                      className="hand"
                      onClick={sort('dateTime')}
                    >
                      Heure <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                    </th>

                    <th
                      style={{
                        fontSize: '14px',
                        position: 'sticky',
                        top: '0',
                        backgroundColor: 'white',
                        width: '40%',
                        textAlign: 'center',
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
                    fontSize: '14px',
                    borderBottom: '50px solid white',
                    textAlign: 'center',
                    backgroundImage: 'url(content/images/NgirwiLogo.png)',
                    backgroundRepeat: 'no-repeat',
                    backgroundAttachment: 'fixed',
                    backgroundPosition: '50% 165%',
                  }}
                >
                  {filter === null
                    ? consultationList.map((consultation, i) => (
                        <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                          <td>
                            <Button
                              tag={Link}
                              to={`/consultation/${consultation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                              data-cy="entityDeleteButton"
                              style={{ color: 'red', backgroundColor: '#F6FAFF', borderColor: '#F6FAFF' }}
                            >
                              {React.createElement(BiTrash, { size: '15' })}
                            </Button>
                          </td>
                          {patientList?.map((patient, b) =>
                            consultation?.patient?.id === patient?.id ? (
                              <>
                                {/* <td>
                                  <Button tag={Link} to={`/patient/${patient.id}`} color="link" size="sm">
                                    {patient.id}
                                  </Button>
                                </td> */}
                                <td>{patient.lastName.toUpperCase()}</td>
                                <td style={{ wordBreak: 'break-all', textTransform: 'capitalize' }}>{patient.firstName}</td>
                                <td>
                                  {consultation.dateTime ? (
                                    <TextFormat type="date" value={consultation.dateTime} format="DD/MM/YYYY" />
                                  ) : null}
                                </td>
                                <td>
                                  {consultation.dateTime
                                    ? // <TextFormat type="date" value={consultation.dateTime} format="HH:mm:ss" />
                                      convertDateTimeFromServerToHours(consultation.dateTime)
                                    : null}
                                </td>
                                <td>
                                  <div
                                    style={{
                                      display: 'flex',
                                      flexDirection: 'row',
                                      gap: '3px',
                                      fontSize: '9px',
                                    }}
                                  >
                                    <Button
                                      tag={Link}
                                      to={`/consultation/${consultation.id}/edit`}
                                      color="primary"
                                      size="sm"
                                      data-cy="entityEditButton"
                                    >
                                      <FontAwesomeIcon icon="pencil" /> <span className="d-none d-md-inline">Mettre à jour</span>
                                    </Button>
                                    <Button
                                      tag={Link}
                                      to={`/consultation/list/${patient.id}?page=1&sort=id,asc`}
                                      color="dark"
                                      size="sm"
                                      data-cy="entityDetailsButton"
                                    >
                                      <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir sa liste</span>
                                    </Button>
                                    <Button
                                      tag={Link}
                                      to={`/prescription/new/${consultation.id}`}
                                      color="success"
                                      size="sm"
                                      data-cy="entityDetailsButton"
                                    >
                                      <FontAwesomeIcon icon="plus" /> <span className="d-none d-md-inline">Ordonnance</span>
                                    </Button>
                                  </div>
                                </td>
                              </>
                            ) : null
                          )}
                        </tr>
                      ))
                    : filter.map((consultation, i) => (
                        <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                          <td>
                            <Button
                              tag={Link}
                              to={`/consultation/${consultation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                              data-cy="entityDeleteButton"
                              style={{ color: 'red', backgroundColor: '#F6FAFF', borderColor: '#F6FAFF' }}
                            >
                              {React.createElement(BiTrash, { size: '15' })}
                            </Button>
                          </td>

                          <td>
                            <Button tag={Link} to={`/patient/${consultation.patient.id}`} color="link" size="sm">
                              {consultation.patient.id}
                            </Button>
                          </td>
                          <td>{consultation.patient.lastName.toUpperCase()}</td>
                          <td style={{ wordBreak: 'break-all', textTransform: 'capitalize' }}>{consultation.patient.firstName}</td>
                          <td>
                            {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="DD/MM/YYYY" /> : null}
                          </td>
                          <td>
                            {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="HH:mm:ss" /> : null}
                          </td>
                          <td>
                            <div
                              style={{
                                display: 'flex',
                                flexDirection: 'row',
                                gap: '3px',
                                fontSize: '9px',
                              }}
                            >
                              <Button
                                tag={Link}
                                to={`/consultation/${consultation.id}/edit`}
                                color="primary"
                                size="sm"
                                data-cy="entityEditButton"
                              >
                                <FontAwesomeIcon icon="pencil" /> <span className="d-none d-md-inline">Mettre à jour</span>
                              </Button>
                              <Button
                                tag={Link}
                                to={`/consultation/list/${consultation.patient.id}?page=1&sort=id,asc`}
                                color="dark"
                                size="sm"
                                data-cy="entityDetailsButton"
                              >
                                <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir sa liste</span>
                              </Button>
                              <Button
                                tag={Link}
                                to={`/prescription/new/${consultation.id}`}
                                color="success"
                                size="sm"
                                data-cy="entityDetailsButton"
                              >
                                <FontAwesomeIcon icon="plus" /> <span className="d-none d-md-inline">Ordonnance</span>
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                </tbody>
              </Table>
            ) : (
              !loading && <div className="alert alert-warning">Aucune consultation enregistrée</div>
            )}
          </Card>
        </div>
      </div>
    </>
  );
};

export default Consultation;
