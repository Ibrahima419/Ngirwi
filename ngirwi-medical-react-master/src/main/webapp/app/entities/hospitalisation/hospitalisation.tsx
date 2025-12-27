import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { TextFormat, getSortState, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams, formatFcfa } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHospitalisation } from 'app/shared/model/hospitalisation.model';
import { getEntities } from './hospitalisation.reducer';
import { getEntity as getPatient, getEntitiesBis } from '../patient/patient.reducer';
// header rendered inline on this page for tighter control

export const Hospitalisation = () => {
  const dispatch = useAppDispatch();
  const { idPatient } = useParams<'idPatient'>();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const [search, setSearch] = useState('');
  const [criteria, setCriteria] = useState(' ');
  const [statusFilter, setStatusFilter] = useState<'ALL' | 'STARTED' | 'ONGOING' | 'DONE'>('ALL');

  const patientEntity = useAppSelector(state => state.patient?.entity);
  const patientList = useAppSelector(state => state.patient?.entities);
  const account = useAppSelector(state => state.authentication.account);
  const hospitalisationList = useAppSelector(state => state.hospitalisation?.entities);
  const loading = useAppSelector(state => state.hospitalisation?.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
    if (idPatient) dispatch(getPatient(idPatient));
    // Fetch patient directory to enable name/number search and display fallbacks
    dispatch(
      getEntitiesBis({
        id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0,
        page: 0,
        size: 9999,
        sort: 'id,asc',
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

  const sort = p => () =>
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handleSearch = event => setSearch(event.target.value);

  const base = hospitalisationList || [];
  // Treat "En cours" (ONGOING option) as both STARTED and ONGOING
  const byStatus =
    statusFilter === 'ALL'
      ? base
      : statusFilter === 'ONGOING'
      ? base.filter(h => h.status === 'STARTED' || h.status === 'ONGOING')
      : base.filter(h => h.status === statusFilter);
  let filter: IHospitalisation[] = byStatus;
  if (search !== '') {
    const q = search.trim().toLowerCase();
    const idFromSearch = q.replace(/[^0-9]/g, '');
    filter = byStatus.filter(h => {
      // match by patient id (e.g., "P1051" or "1051")
      const pid = String((h as any).patientId ?? (h as any).patient?.id ?? '');
      const matchesId = pid !== '' && (pid === idFromSearch || `p${pid}` === q);

      // match by patient last/first name using directory
      const patient = patientList?.find(p => String(p.id) === pid);
      const fullName = patient ? `${(patient.lastName || '').toLowerCase()} ${(patient.firstName || '').toLowerCase()}` : '';
      const matchesName = fullName.includes(q);

      // match by date substring as a convenience
      const matchesDate = h.entryDate ? h.entryDate.toString().toLowerCase().includes(q) : false;

      return matchesId || matchesName || matchesDate;
    });
  }

  function getDuration(dateString: string): string {
    if (!dateString) return '';
    const dateParts = dateString.split('T');
    if (dateParts.length !== 2) return '';
    const [year, month, day] = dateParts[0].split('-');
    const [hour, minute] = dateParts[1].split(':');
    const eventDate = new Date(Number(`${year}`), Number(month) - 1, Number(day), Number(hour), Number(minute));
    const durationMs = new Date().getTime() - eventDate.getTime();
    const durationMinutes = Math.floor(durationMs / (1000 * 60));
    const durationHours = Math.floor(durationMinutes / 60);
    const durationDays = Math.floor(durationHours / 24);
    return `${durationDays} jours, ${durationHours % 24} heures, ${durationMinutes % 60} minutes`;
  }

  const countOngoing = base.filter(h => h.status === 'STARTED' || h.status === 'ONGOING').length;
  const countDone = base.filter(h => h.status === 'DONE').length;
  const totalAmount = base.reduce((acc, h) => acc + (h.totalAmount || 0), 0);

  return (
    <div
      style={{ paddingLeft: '16vw', paddingTop: '1%', fontFamily: 'Mulish', fontWeight: '900', display: 'flex', flexDirection: 'column' }}
    >
      <div style={{ margin: '8px 24px 12px 24px' }}>
        <div style={{ fontWeight: 800, fontSize: '28px', lineHeight: 1.1, textAlign: 'left' }}>Hospitalisations</div>
        <div style={{ color: '#6c757d', fontSize: '14px', fontWeight: 400, marginTop: '4px', textAlign: 'left' }}>
          Gestion des séjours des patients
        </div>
      </div>
      <div style={{ display: 'flex', justifyContent: 'flex-end', margin: '8px 24px 8px 0' }}>
        {account?.authorities?.some(role => role === 'ROLE_ADMIN' || role === 'ROLE_DOCTOR') && (
        <Link to={idPatient ? `/hospitalisation/new/${idPatient}` : '/hospitalisation/new'} className="btn btn-primary">
          + Nouvelle hospitalisation
        </Link>
      )}
      </div>
      <div style={{ display: 'flex', flexDirection: 'column' }}>
        {/* KPI Row */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '24px', margin: '0 24px 16px 24px' }}>
          <Card
            style={{
              padding: '20px',
              borderRadius: '12px',
              background: 'linear-gradient(135deg, #e7f7ef 0%, #bff0d6 100%)',
              border: '1px solid #d7f3e5',
            }}
          >
            <div style={{ fontSize: '28px', fontWeight: 800, textAlign: 'left' }}>{countOngoing}</div>
            <div style={{ color: '#6c757d' }}>En cours</div>
          </Card>
          <Card
            style={{
              padding: '20px',
              borderRadius: '12px',
              background: 'linear-gradient(135deg, #fdeceb 0%, #f8b4b4 100%)',
              border: '1px solid #f4c7c7',
            }}
          >
            <div style={{ fontSize: '28px', fontWeight: 800, textAlign: 'left' }}>{countDone}</div>
            <div style={{ color: '#6c757d' }}>Fermées</div>
          </Card>
          <Card
            style={{
              padding: '20px',
              borderRadius: '12px',
              background: 'linear-gradient(135deg, #fff3e6 0%, #ffd6a6 100%)',
              border: '1px solid #ffe0bf',
            }}
          >
            <div style={{ fontSize: '28px', fontWeight: 800, textAlign: 'left' }}>{formatFcfa(totalAmount)}</div>
            <div style={{ color: '#6c757d' }}>Montant total</div>
          </Card>
        </div>

        {/* Search + Filter Row */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 220px', gap: '16px', alignItems: 'center', margin: '0 24px 16px 24px' }}>
          <ValidatedField
            style={{ borderRadius: '12px', width: '100%' }}
            placeholder="Rechercher par nom, prénom ou numéro patient…"
            id="search"
            name="search"
            type="text"
            onChange={e => setSearch(e.target.value)}
          />
          <ValidatedField
            style={{ borderRadius: '12px', width: '100%' }}
            id="statusFilter"
            name="statusFilter"
            type="select"
            onChange={e => setStatusFilter(e.target.value as any)}
          >
            <option value="ALL">Tous les statuts</option>
            <option value="ONGOING">En cours</option>
            <option value="DONE">Fermées</option>
          </ValidatedField>
        </div>

        <Card
          style={{
            width: '83vw',
            backgroundColor: 'white',
            borderRadius: '12px',
            boxShadow: '0 2px 10px rgba(16, 24, 40, 0.06)',
            marginLeft: '24px',
          }}
        >
          <div style={{ display: 'flex', flexDirection: 'row', marginTop: '1%' }}>
            <span style={{ color: '#141414', fontSize: '20px', marginLeft: '3%', marginBottom: '1%', width: '45vw' }}>
              Hospitalisations enregistrées
            </span>

            {/* toolbar moved above; keep header only */}
          </div>
          {patientEntity?.id ? (
            <div style={{ marginLeft: '3%', fontSize: '15px' }}>
              <span>Patient:</span>
              <span style={{ textTransform: 'uppercase' }}>{' ' + patientEntity.lastName + ' '}</span>
              <span style={{ textTransform: 'capitalize' }}>{patientEntity.firstName}</span>{' '}
              <div>
                <span>Pièce d&apos;identité n°</span>
                <span style={{ textTransform: 'uppercase' }}>{' ' + (patientEntity.cni || '')}</span>
              </div>
            </div>
          ) : null}

          {hospitalisationList && hospitalisationList.length > 0 ? (
            <div
              style={{
                height: 'calc(75vh - 260px)', 
                overflowY: 'auto',
                overflowX: 'hidden',
              }}
            >
              <Table responsive className="mb-0 w-100" style={{ tableLayout: 'fixed' }}>
              <colgroup>
                <col style={{ width: '25%' }} />
                <col style={{ width: '15%' }} />
                <col style={{ width: '20%' }} />
                <col style={{ width: '15%' }} />
                <col style={{ width: '10%' }} />
                <col style={{ width: '15%' }} />
              </colgroup>
              <thead className="sticky-top">
                <tr className="text-muted small">
                  <th className="text-start fw-bold">Patient</th>
                  <th className="text-start fw-bold d-none d-md-table-cell">Service</th>
                  <th className="fw-bold">Dates</th>
                  <th className="text-center fw-bold">Statut</th>
                  <th className="text-end fw-bold">Coût</th>
                  <th className="text-center fw-bold">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filter.map((h, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable" className="align-middle">
                    {/* Patient */}
                    <td className="px-3 py-2 text-start">
                      {(h as any).patient ? (
                        <>
                          <div className="fw-semibold text-dark" style={{ fontSize: '14px' }}>
                            {(((h as any).patient.lastName || '') as string).toUpperCase()} {(h as any).patient.firstName || ''}
                          </div>
                          <div className="text-muted" style={{ fontSize: '12px' }}>
                            P{(h as any).patient.id}
                          </div>
                        </>
                      ) : (
                        (() => {
                          const pid = (h as any).patientId;
                          const p = patientList?.find(pp => String(pp.id) === String(pid));
                          const name = p ? `${(p.lastName || '').toUpperCase()} ${p.firstName || ''}` : undefined;
                          return (
                            <>
                              <div className="fw-semibold text-dark" style={{ fontSize: '14px' }}>
                                {name || '—'}
                              </div>
                              <div className="text-muted" style={{ fontSize: '12px' }}>
                                P{pid ?? '—'}
                              </div>
                            </>
                          );
                        })()
                      )}
                    </td>

                    {/* Service (hidden on small) */}
                    <td className="px-3 py-2 text-start d-none d-md-table-cell">
                      <div className="fw-semibold" style={{ fontSize: '14px' }}>
                        {h.service || '—'}
                      </div>
                      <div className="text-muted" style={{ fontSize: '12px' }}>
                        {(h.service || '—').slice(0, 4).toUpperCase()}
                      </div>
                    </td>

                    {/* Dates */}
                    <td className="px-3 py-2" style={{ whiteSpace: 'nowrap' }}>
                      <div className="text-dark">
                        {h.entryDate ? <TextFormat type="date" value={h.entryDate} format={APP_DATE_FORMAT} /> : '—'}
                      </div>
                      <div className="text-muted" style={{ fontSize: '12px' }}>
                        {h.releaseDate ? (
                          <>
                            <span>– </span>
                            <TextFormat type="date" value={h.releaseDate} format={APP_DATE_FORMAT} />
                          </>
                        ) : null}
                      </div>
                    </td>

                    {/* Status */}
                    <td className="px-3 py-2 text-center">
                      {(() => {
                        const st = (h.status || '').toString();
                        const isDone = st === 'DONE';
                        const label = isDone ? 'FERMÉE' : 'EN COURS';
                        const cls = isDone ? 'badge rounded-pill bg-danger' : 'badge rounded-pill bg-info';
                        return (
                          <span className={cls} style={{ fontWeight: 600 }}>
                            {label}
                          </span>
                        );
                      })()}
                    </td>

                    {/* Cost */}
                    <td className="px-3 py-2 text-end" style={{ whiteSpace: 'nowrap' }}>
                      {formatFcfa(h.totalAmount || 0)}
                    </td>

                    {/* Actions */}
                    <td className="px-3 py-2 text-center">
                      <div className="btn-group" role="group" aria-label="actions">
                        <Button
                          tag={Link}
                          to={`/hospitalisation/${h.id}`}
                          color="secondary"
                          size="sm"
                          outline
                          className="me-2"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" className="me-1" /> Voir
                        </Button>
                        {account?.authorities?.some(role => role === 'ROLE_ADMIN' || role === 'ROLE_DOCTOR') && (
                        <Button
                          tag={Link}
                          to={`/hospitalisation/${h.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          outline
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" className="me-1" /> Éditer
                        </Button>
                      )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            </div>
          ) : (
            !loading && <div className="alert alert-warning">Aucune Hospitalisation trouvée</div>
          )}
        </Card>
      </div>
    </div>
  );
};

export default Hospitalisation;
