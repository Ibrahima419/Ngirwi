import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDossierMedical } from 'app/shared/model/dossier-medical.model';
import { getEntities } from './dossier-medical.reducer';
import Header from 'app/shared/layout/header/header';
import { RiUserAddLine } from 'react-icons/ri';
import { Scrollbars } from 'react-custom-scrollbars';
import TableScrollbar from 'react-table-scrollbar';

export const DossierMedical = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const dossierMedicalList = useAppSelector(state => state.dossierMedical.entities);
  const loading = useAppSelector(state => state.dossierMedical.loading);
  const totalItems = useAppSelector(state => state.dossierMedical.totalItems);

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
      case 'dateCreated':
        filter = dossierMedicalList.filter(dossierMedical => {
          return dossierMedical.dateCreated.includes(search);
        });
        break;
      case 'dateUpdated':
        filter = dossierMedicalList.filter(dossierMedical => {
          return dossierMedical.dateUpdated.includes(search);
        });
        break;
      case 'author':
        filter = dossierMedicalList.filter(dossierMedical => {
          return dossierMedical.author.toLowerCase().includes(search.toLowerCase());
        });
        break;
      case 'patient':
        filter = dossierMedicalList.filter(dossierMedical => {
          const all = dossierMedical.patient.lastName + ' ' + dossierMedical.patient.firstName;
          return all.toLowerCase().includes(search.toLowerCase());
        });
        break;
      default:
        filter = null;
    }
  }

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
      <Header pageName={'Dossiers médicaux'} />
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
          <div
            style={{
              justifyContent: 'center',
              alignItems: 'center',
              minWidth: '15vw',
              minHeight: '15vw',
              borderRadius: '50%',
              backgroundColor: '#CBDCF7',
              paddingTop: '25%',
              paddingLeft: '4%',
              cursor: 'pointer',
              display: 'inline-block',
            }}
          >
            <span
              onClick={() => handleSyncList()}
              style={{
                display: 'block',
                fontFamily: 'Ubuntu',
                color: '#56B5C5',
                fontSize: '18px',
                textAlign: 'center',
              }}
            >
              <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
            </span>
          </div>

          <Card
            style={{
              height: '6.28vh',
              width: '33.38vw',
              borderRadius: '20px',
              backgroundColor: '#11485C',
              textAlign: 'center',
              color: 'white',
              marginBottom: '5vh',
              boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            }}
          >
            <span style={{ marginTop: '1.5%' }}>Liste des dossiers médicaux</span>
          </Card>

          <Link
            to="#"
            style={{
              display: 'inline-block',
              textDecoration: 'none',
              textAlign: 'center',
              color: '#56B5C5',
              minWidth: '15vw',
              minHeight: '15vw',
              borderRadius: '50%',
              backgroundColor: '#CBDCF7',
              fontSize: '18px',
            }}
          >
            <span style={{ display: 'block', marginTop: '20%' }}>
              {React.createElement(RiUserAddLine, { size: '24' })} Enregistrer nouveau dossier
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
              Dossiers médicaux enregistrés
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
                <option value="dateCreated">Crée le</option>
                <option value="dateUpdated">Mise à jour le</option>
                <option value="author">Auteur</option>
                <option value="patient">Patient</option>
                {/* </select> */}
              </ValidatedField>
              <ValidatedField
                style={{ borderRadius: '12px', width: '17vw' }}
                placeholder="Barre de recherche"
                id="search"
                name="search"
                type={criteria === 'dateCreated' || criteria === 'dateUpdated' ? 'date' : 'text'}
                onChange={handleSearch}
              />
              {/* <input type="text" id="search" name="search" placeholder="Barre de recherche" onChange={handleSearch} />  */}
            </div>
          </div>

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
                    textAlign: 'center',
                    fontSize: '14px',
                    position: 'sticky',
                    top: '0',
                    width: '10%',
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
                    width: '15%',
                    backgroundColor: 'white',
                  }}
                  className="hand"
                  onClick={sort('dateCreated')}
                >
                  Date de création <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                </th>
                <th
                  style={{
                    textAlign: 'center',
                    fontSize: '14px',
                    position: 'sticky',
                    top: '0',
                    width: '15%',
                    backgroundColor: 'white',
                  }}
                  className="hand"
                  onClick={sort('dateUpdated')}
                >
                  Date de mise à jour <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                </th>
                <th
                  style={{
                    textAlign: 'center',
                    fontSize: '14px',
                    position: 'sticky',
                    top: '0',
                    width: '17%',
                    backgroundColor: 'white',
                  }}
                  className="hand"
                  onClick={sort('author')}
                >
                  Auteur <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                </th>
                <th
                  style={{
                    textAlign: 'center',
                    fontSize: '14px',
                    position: 'sticky',
                    top: '0',
                    width: '17%',
                    backgroundColor: 'white',
                  }}
                >
                  Patient <FontAwesomeIcon style={{ marginLeft: '10px' }} icon="sort" />
                </th>
                <th
                  style={{
                    textAlign: 'center',
                    fontSize: '14px',
                    position: 'sticky',
                    top: '0',
                    width: '45%',
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
                backgroundImage: 'url(content/images/NgirwiLogo.png)',
                backgroundRepeat: 'no-repeat',
                backgroundAttachment: 'fixed',
                backgroundPosition: '65% 90%',
                backgroundSize: '50% 50%',
              }}
            >
              {filter === null
                ? dossierMedicalList.map((dossierMedical, i) => (
                    <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                      <td>
                        <Button tag={Link} to={`/dossier-medical/${dossierMedical.id}`} color="link" size="sm">
                          {dossierMedical.id}
                        </Button>
                      </td>
                      <td>
                        {dossierMedical.dateCreated ? (
                          <TextFormat type="date" value={dossierMedical.dateCreated} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>
                        {dossierMedical.dateUpdated ? (
                          <TextFormat type="date" value={dossierMedical.dateUpdated} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td style={{ wordBreak: 'break-all' }}>{'Dr ' + dossierMedical.author}</td>
                      <td style={{ wordBreak: 'break-all' }}>
                        {dossierMedical.patient ? (
                          <span>{dossierMedical.patient.lastName.toUpperCase() + ' ' + dossierMedical.patient.firstName}</span>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-end">
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
                            to={`/dossier-medical/${dossierMedical.id}/edit/${'voir'}`}
                            color="info"
                            size="sm"
                            data-cy="entityDetailsButton"
                          >
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/dossier-medical/${dossierMedical.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/dossier-medical/${dossierMedical.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))
                : filter.map((dossierMedical, i) => (
                    <tr style={{ border: '1px solid #E9F1FF', borderRadius: '15px' }} key={`entity-${i}`} data-cy="entityTable">
                      <td>
                        <Button tag={Link} to={`/dossier-medical/${dossierMedical.id}`} color="link" size="sm">
                          {dossierMedical.id}
                        </Button>
                      </td>
                      <td>
                        {dossierMedical.dateCreated ? (
                          <TextFormat type="date" value={dossierMedical.dateCreated} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>
                        {dossierMedical.dateUpdated ? (
                          <TextFormat type="date" value={dossierMedical.dateUpdated} format={APP_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td style={{ wordBreak: 'break-all' }}>{'Dr ' + dossierMedical.author}</td>
                      <td style={{ wordBreak: 'break-all' }}>
                        {dossierMedical.patient ? (
                          <span>{dossierMedical.patient.lastName.toUpperCase() + ' ' + dossierMedical.patient.firstName}</span>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-end">
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
                            to={`/dossier-medical/${dossierMedical.id}/edit/${'voir'}`}
                            color="info"
                            size="sm"
                            data-cy="entityDetailsButton"
                          >
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/dossier-medical/${dossierMedical.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                          </Button>
                          <Button
                            tag={Link}
                            to={`/dossier-medical/${dossierMedical.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="danger"
                            size="sm"
                            data-cy="entityDeleteButton"
                          >
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
            </tbody>
          </Table>
        </Card>
      </div>
      {/* <h2 id="dossier-medical-heading" data-cy="DossierMedicalHeading">
        Dossiers Médicaux
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/dossier-medical/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Dossier Medical
          </Link>
        </div>
      </h2> */}
      {/* <div className="table-responsive">
        {dossierMedicalList && dossierMedicalList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('motifConsultation')}>
                  Motif Consultation <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('histoireMaladie')}>
                  Histoire Maladie <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('terrain')}>
                  Terrain <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('antecedantsPersonnels')}>
                  Antecedants Personnels <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('antecedantsChirurgicaux')}>
                  Antecedants Chirurgicaux <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('antecedantsFamiliaux')}>
                  Antecedants Familiaux <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gynecoObstretrique')}>
                  Gyneco Obstretrique <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('syndromique')}>
                  Résumé Syndromique <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dad')}>
                  Père <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mom')}>
                  Mère <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('siblings')}>
                  Frères <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('descendants')}>
                  Descendants <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateCreated')}>
                  Date de création <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateUpdated')}>
                  Date de mise à jour <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('author')}>
                  Autheur <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Patient <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dossierMedicalList.map((dossierMedical, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dossier-medical/${dossierMedical.id}`} color="link" size="sm">
                      {dossierMedical.id}
                    </Button>
                  </td>
                  <td>{dossierMedical.motifConsultation}</td>
                  <td>{dossierMedical.histoireMaladie}</td>
                  <td>{dossierMedical.terrain}</td>
                  <td>{dossierMedical.antecedantsPersonnels}</td>
                  <td>{dossierMedical.antecedantsChirurgicaux}</td>
                  <td>{dossierMedical.antecedantsFamiliaux}</td>
                  <td>{dossierMedical.gynecoObstretrique}</td>
                  <td>{dossierMedical.syndromique}</td>
                  <td>{dossierMedical.dad}</td>
                  <td>{dossierMedical.mom}</td>
                  <td>{dossierMedical.siblings}</td>
                  <td>{dossierMedical.descendants}</td>
                  <td>
                    {dossierMedical.dateCreated ? (
                      <TextFormat type="date" value={dossierMedical.dateCreated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {dossierMedical.dateUpdated ? (
                      <TextFormat type="date" value={dossierMedical.dateUpdated} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{dossierMedical.author}</td>
                  <td>
                    {dossierMedical.patient ? <Link to={`/patient/${dossierMedical.patient.id}`}>{dossierMedical.patient.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button 
                        tag={Link} 
                        to={`/dossier-medical/${dossierMedical.id}/edit/${"voir"}`}
                        color="info" 
                        size="sm" 
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/dossier-medical/${dossierMedical.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/dossier-medical/${dossierMedical.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Aucun Dossier Medical trouvé</div>
        )}
      </div>
      {totalItems ? (
        <div className={dossierMedicalList && dossierMedicalList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )} */}
    </div>
  );
};

export default DossierMedical;
