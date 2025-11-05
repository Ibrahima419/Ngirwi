import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { Button, Card, Input, Table } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getBySurveillanceSheet, selectMiniConsultationsBySheet, selectMiniConsultationTotalsBySheet } from './mini-consultation.reducer';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { formatFcfa } from 'app/shared/util/entity-utils';

const MiniConsultationList = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { surveillanceSheetId } = useParams<'surveillanceSheetId'>();
  const sid = Number(surveillanceSheetId);

  const [query, setQuery] = useState('');

  useEffect(() => {
    if (sid) dispatch(getBySurveillanceSheet({ surveillanceSheetId: sid }));
  }, [sid]);

  const items = useAppSelector(state => selectMiniConsultationsBySheet(state, sid));
  const totals = useAppSelector(state => selectMiniConsultationTotalsBySheet(state, sid));

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;
    return (items || []).filter(
      i =>
        (i.summary || '').toLowerCase().includes(q) ||
        (i.diagnosis || '').toLowerCase().includes(q) ||
        (i.authorName || '').toLowerCase().includes(q)
    );
  }, [items, query]);

  return (
    <div style={{ padding: '1rem' }}>
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
        <Card style={{ padding: '1rem', width: '18rem' }}>
          <div>Nombre</div>
          <div style={{ fontSize: '1.4rem' }}>{totals.count}</div>
        </Card>
        <Card style={{ padding: '1rem', width: '18rem' }}>
          <div>Total</div>
          <div style={{ fontSize: '1.4rem' }}>{formatFcfa(totals.total)}</div>
        </Card>
        <div style={{ flex: 1 }} />
        <Button color="primary" onClick={() => navigate(`/surveillance-sheet/${sid}/mini-consultations/new`)}>
          + Nouvelle mini-consultation
        </Button>
      </div>

      <div className="d-flex align-items-center gap-2 mb-3">
        <Input placeholder="Rechercher (résumé, diagnostic, auteur)" value={query} onChange={e => setQuery(e.target.value)} />
      </div>

      <Table responsive>
        <thead>
          <tr>
            <th>#</th>
            <th>Résumé</th>
            <th>Diagnostic</th>
            <th>Auteur</th>
            <th>Créée</th>
            <th>Prix</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {(filtered || []).map(m => (
            <tr key={m.id}>
              <td>{m.id}</td>
              <td>{m.summary}</td>
              <td>{m.diagnosis}</td>
              <td>{m.authorName || '—'}</td>
              <td>{m.createdDate ? <TextFormat value={m.createdDate} type="date" format={APP_DATE_FORMAT} /> : '—'}</td>
              <td>{formatFcfa(Number(m.price || 0))}</td>
              <td className="text-end">
                <div className="btn-group">
                  <Button size="sm" color="info" tag={Link} to={`/surveillance-sheet/${sid}/mini-consultations/${m.id}`}>
                    Voir
                  </Button>
                  <Button size="sm" color="primary" tag={Link} to={`/surveillance-sheet/${sid}/mini-consultations/${m.id}/edit`}>
                    Modifier
                  </Button>
                  <Button size="sm" color="danger" tag={Link} to={`/surveillance-sheet/${sid}/mini-consultations/${m.id}/delete`}>
                    Supprimer
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default MiniConsultationList;
