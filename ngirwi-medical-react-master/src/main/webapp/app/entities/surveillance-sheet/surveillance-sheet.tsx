import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Card, CardBody } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISurveillanceSheet } from 'app/shared/model/surveillance-sheet.model';
import { getEntities } from './surveillance-sheet.reducer';

export const SurveillanceSheet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const surveillanceSheetList = useAppSelector(state => state.surveillanceSheet.entities);
  const loading = useAppSelector(state => state.surveillanceSheet.loading);
  const totalItems = useAppSelector(state => state.surveillanceSheet.totalItems);

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

  const count = surveillanceSheetList?.length || 0;

  return (
    <div
      className="text-dark"
      style={{ paddingLeft: '16vw', paddingTop: '1%', fontFamily: 'Mulish', display: 'flex', flexDirection: 'column' }}
    >
      <div className="d-flex align-items-center justify-content-between mb-2">
        <div>
          <h2 id="surveillance-sheet-heading" data-cy="SurveillanceSheetHeading" className="mb-0">
            Fiches de surveillance
          </h2>
          <div className="text-muted small">
            {count} fiche{count > 1 ? 's' : ''}
          </div>
        </div>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link
            to="/surveillance-sheet/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Surveillance Sheet
          </Link>
        </div>
      </div>

      <Card className="shadow-sm" style={{ marginRight: '16px' }}>
        <CardBody className="table-responsive">
          {surveillanceSheetList && surveillanceSheetList.length > 0 ? (
            <Table responsive className="table-sm table-striped align-middle">
              <thead className="sticky-top bg-white">
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('sheetDate')}>
                    Date de fiche <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('temperature')}>
                    Température <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('systolicBP')}>
                    TA Systolique <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('diastolicBP')}>
                    TA Diastolique <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('pulseRate')}>
                    Pouls <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('respirationRate')}>
                    Respiration <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('spo2')}>
                    SaO2 <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>Notes soignantes</th>
                  <th>Observations médicales</th>
                  <th>
                    Hospitalisation <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {surveillanceSheetList.map((surveillanceSheet, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/surveillance-sheet/${surveillanceSheet.id}`} color="link" size="sm">
                        {surveillanceSheet.id}
                      </Button>
                    </td>
                    <td>{surveillanceSheet.sheetDate}</td>
                    <td>{surveillanceSheet.temperature}</td>
                    <td>{surveillanceSheet.systolicBP}</td>
                    <td>{surveillanceSheet.diastolicBP}</td>
                    <td>{surveillanceSheet.pulseRate}</td>
                    <td>{surveillanceSheet.respirationRate}</td>
                    <td>{surveillanceSheet.spo2}</td>
                    <td style={{ maxWidth: 220, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                      {(surveillanceSheet.nursingNotes || '').toString()}
                    </td>
                    <td style={{ maxWidth: 220, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                      {(surveillanceSheet.medicalObservations || '').toString()}
                    </td>
                    <td>
                      {(() => {
                        const hid = (surveillanceSheet as any).hospitalisation?.id || (surveillanceSheet as any).hospitalisationId;
                        if (!hid) return '—';
                        return (
                          <Link to={`/hospitalisation/${hid}`} className="badge rounded-pill bg-light text-primary border">
                            {hid}
                          </Link>
                        );
                      })()}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/surveillance-sheet/${surveillanceSheet.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/surveillance-sheet/${surveillanceSheet.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/surveillance-sheet/${surveillanceSheet.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
            !loading && <div className="alert alert-warning mb-0">Aucun Surveillance Sheet trouvé</div>
          )}
        </CardBody>
      </Card>
      {totalItems ? (
        <div className={surveillanceSheetList && surveillanceSheetList.length > 0 ? '' : 'd-none'}>
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
      )}
    </div>
  );
};

export default SurveillanceSheet;
