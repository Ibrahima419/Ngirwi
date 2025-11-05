import { useAppDispatch, useAppSelector } from 'app/config/store';
import Header from 'app/shared/layout/header/header';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import React, { useEffect, useState } from 'react';
import { BiTrash } from 'react-icons/bi';
import { getSortState, TextFormat } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { IoIosArrowBack } from 'react-icons/io';
import { getEntities } from './consultation.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const ConsultationPatient = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const loading = useAppSelector(state => state.consultation.loading);
  const { idPatient } = useParams<'idPatient'>();
  const patients = useAppSelector(state => state.patient.entities);
  const consultationList = useAppSelector(state => state.consultation.entities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const getAllEntities = () => {
    dispatch(
      getEntities({
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

  return (
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
        <Card
          style={{
            height: '6.28vh',
            minWidth: '30vw',
            borderRadius: '20px',
            backgroundColor: '#11485C',
            textAlign: 'center',
            color: 'white',
            marginBottom: '5vh',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'center',
            alignItems: 'center',
            marginLeft: '25vw',
          }}
        >
          <Button onClick={() => window.history.back()} style={{ color: '#53BFD1', backgroundColor: '#11485C', borderColor: '#11485C' }}>
            {React.createElement(IoIosArrowBack, { size: '20' })}
          </Button>
          <span>Liste consultations patient </span>
        </Card>

        <Card
          style={{
            width: '83vw',
            minHeight: '3vh',
            backgroundColor: 'white',
            position: 'fixed',
            top: '32vh',
            marginRight: '1%',
            borderRadius: '15px 15px 0px 0px',
            boxShadow: '0px 2px 12px 4px rgba(138, 161, 203, 0.23)',
          }}
        >
          {/* <span style={{marginTop:"1%", color:"#141414",fontSize:"20px", marginLeft:"3%", marginBottom:"1%"}}>Liste consultations patient</span> */}
          <span
            style={{
              marginTop: '2%',
              color: '#141414',
              fontSize: '25px',
              marginBottom: '3%',
              textAlign: 'center',
            }}
          >
            {patients.map(patient =>
              patient.id.toString() === idPatient ? (
                <div>
                  {'Patient: ' + patient.lastName.toUpperCase() + ' ' + patient.firstName}
                  <br />
                  <span style={{ fontWeight: '400' }}>{'Matricule: #' + patient.id}</span>
                </div>
              ) : null
            )}
          </span>
          {consultationList && consultationList.length > 0 ? (
            <Table style={{ width: '82vw', borderCollapse: 'separate', borderSpacing: '0 15px' }} responsive>
              <thead>
                <tr>
                  <th
                    style={{
                      position: 'sticky',
                      top: '0',
                      width: '3.32vw',
                      backgroundColor: 'white',
                    }}
                  ></th>
                  {/* <th 
              style={{
                textAlign:"center",
                fontSize:"14px",
                position:"sticky",
                top:"0",
                width:"24%",
                backgroundColor:"white",
              }}
                className="hand" onClick={sort('id')}>
                ID 
              </th> */}
                  <th
                    style={{
                      textAlign: 'center',
                      fontSize: '14px',
                      position: 'sticky',
                      top: '0',
                      width: '19.92vw',
                      backgroundColor: 'white',
                    }}
                    className="hand"
                    onClick={sort('dateTime')}
                  >
                    Date <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                  </th>
                  <th
                    style={{
                      textAlign: 'center',
                      fontSize: '14px',
                      position: 'sticky',
                      top: '0',
                      width: '19.92vw',
                      backgroundColor: 'white',
                    }}
                    className="hand"
                    onClick={sort('dateTime')}
                  >
                    Heure <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                  </th>
                  <th
                    style={{
                      textAlign: 'center',
                      fontSize: '14px',
                      position: 'sticky',
                      top: '0',
                      width: '19.92vw',
                      backgroundColor: 'white',
                      wordWrap: 'break-word',
                      textOverflow: 'ellipsis',
                    }}
                    className="hand"
                    onClick={sort('hypothesis')}
                  >
                    Hypothèse diagnostique <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                  </th>

                  <th
                    style={{
                      textAlign: 'center',
                      fontSize: '14px',
                      position: 'sticky',
                      top: '0',
                      maxWidth: '19.92vw',
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
                  fontSize: '18px',
                  textAlign: 'center',
                  borderBottom: '50px solid white',
                  wordWrap: 'break-word',
                  textOverflow: 'ellipsis',
                  backgroundImage: 'url(content/images/NgirwiLogo.png)',
                  backgroundRepeat: 'no-repeat',
                  backgroundAttachment: 'fixed',
                  backgroundPosition: '65% 110%',
                  backgroundSize: '50% 50%',
                }}
              >
                {consultationList?.map((consultation, i) =>
                  consultation?.patient?.id.toString() === idPatient ? (
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
                      {/* <td style={{ width: '19.92vw' }}>
                        {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="DD/MM/YYYY" /> : null}
                      </td>
                      <td style={{ width: '19.92vw' }}>
                        {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="HH:mm:ss" /> : null}
                      </td>
                      <td style={{ width: '19.92vw', wordWrap: 'break-word' }}>{consultation.hypothesis}</td>
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
                            to={`/consultation/${consultation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <span className="d-none d-md-inline">Mettre à jour</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/consultation/${consultation.id}/edit/${'voir'}`}
                            color="dark"
                            size="sm"
                            data-cy="entityDetailsButton"
                          >
                            <span className="d-none d-md-inline">Voir détails</span>
                          </Button>
                        </div>
                      </td> */}
                      {patients.map((patient, b) =>
                        consultation.patient.lastName === patient.lastName && consultation.patient.id === patient.id ? (
                          <>
                            {/* <td>
                <Button tag={Link} to={`/patient/${patient.id}`} color="link" size="sm">
                  {patient.id}
                </Button>
              </td> */}
                            <td style={{ width: '19.92vw' }}>
                              {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="DD/MM/YYYY" /> : null}
                            </td>
                            <td style={{ width: '19.92vw' }}>
                              {consultation.dateTime ? <TextFormat type="date" value={consultation.dateTime} format="HH:mm:ss" /> : null}
                            </td>
                            <td style={{ width: '19.92vw', wordWrap: 'break-word' }}>{consultation.hypothesis}</td>
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
                                  to={`/consultation/${consultation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                  color="primary"
                                  size="sm"
                                  data-cy="entityEditButton"
                                >
                                  <span className="d-none d-md-inline">Mettre à jour</span>
                                </Button>
                                <Button
                                  tag={Link}
                                  to={`/consultation/${consultation.id}/edit/${'voir'}`}
                                  color="dark"
                                  size="sm"
                                  data-cy="entityDetailsButton"
                                >
                                  <span className="d-none d-md-inline">Voir détails</span>
                                </Button>
                              </div>
                            </td>
                          </>
                        ) : null
                      )}
                    </tr>
                  ) : null
                )}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">Aucun Consultation trouvé</div>
          )}
        </Card>
      </div>
    </div>
  );
};
export default ConsultationPatient;
