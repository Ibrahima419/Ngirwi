import React, { useEffect, useMemo, useState } from 'react';
import { useLocation, useNavigate, useParams, Link } from 'react-router-dom';
import { Button, Card, Table } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getByHospitalisation, selectSheetsByHospitalisation, selectSheetTotals } from './surveillance-sheet.reducer';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { formatFcfa } from 'app/shared/util/entity-utils';

const SurveillanceSheetList = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { hospitalisationId } = useParams<'hospitalisationId'>();
  const hid = Number(hospitalisationId);

  useEffect(() => {
    if (hid) dispatch(getByHospitalisation({ hospitalisationId: hid }));
  }, [hid]);

  const sheets = useAppSelector(state => selectSheetsByHospitalisation(state, hid));
  const totals = useAppSelector(state => selectSheetTotals(state, hid));
  const [sortKey, setSortKey] = useState<'sheetDate' | 'createdDate'>('sheetDate');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  const sorted = useMemo(() => {
    const cp = [...(sheets || [])];
    cp.sort((a: any, b: any) => {
      const va = a[sortKey] || '';
      const vb = b[sortKey] || '';
      return sortOrder === 'asc' ? String(va).localeCompare(String(vb)) : String(vb).localeCompare(String(va));
    });
    return cp;
  }, [sheets, sortKey, sortOrder]);

  return (
    <div style={{ padding: '1rem' }}>
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
        <Card style={{ padding: '1rem', width: '18rem' }}>
          <div>Nombre de fiches</div>
          <div style={{ fontSize: '1.4rem' }}>{totals.count}</div>
        </Card>
        <Card style={{ padding: '1rem', width: '18rem' }}>
          <div>Coût cumulé</div>
          <div style={{ fontSize: '1.4rem' }}>{formatFcfa(totals.sheetsTotal)}</div>
        </Card>
        <div style={{ flex: 1 }} />
        <Button color="primary" onClick={() => navigate(`/surveillance-sheet/new?hospitalisationId=${hid}`)}>
          + Ajouter une fiche
        </Button>
      </div>

      <div className="d-flex align-items-center gap-2 mb-2">
        <span>Trier par:</span>
        <Button color={sortKey === 'sheetDate' ? 'primary' : 'secondary'} onClick={() => setSortKey('sheetDate')}>
          Date de fiche
        </Button>
        <Button color={sortKey === 'createdDate' ? 'primary' : 'secondary'} onClick={() => setSortKey('createdDate')}>
          Date de création
        </Button>
        <Button color={sortOrder === 'asc' ? 'secondary' : 'secondary'} onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}>
          {sortOrder === 'asc' ? 'Asc' : 'Desc'}
        </Button>
      </div>

      <Table responsive>
        <thead>
          <tr>
            <th>Fiche</th>
            <th>Auteur</th>
            <th>Résumé</th>
            <th>Total</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {sorted.map(s => (
            <tr key={s.id}>
              <td>
                <div>
                  <strong>{s.sheetDate}</strong>
                </div>
                <div>Créée le&nbsp;{s.createdDate ? <TextFormat value={s.createdDate} type="date" format={APP_DATE_FORMAT} /> : '—'}</div>
                <div>ID&nbsp;#{s.id}</div>
              </td>
              <td>{s.authorName || '—'}</td>
              <td>
                {s.medications?.length || 0} médicament(s) · {s.acts?.length || 0} acte(s)
              </td>
              <td>{formatFcfa(Number(s.total || 0))}</td>
              <td className="text-end">
                <div className="btn-group">
                  <Button color="info" size="sm" tag={Link} to={`/surveillance-sheet/${s.id}`}>
                    Voir
                  </Button>
                  <Button color="primary" size="sm" tag={Link} to={`/surveillance-sheet/${s.id}/edit`}>
                    Modifier
                  </Button>
                  <Button color="danger" size="sm" tag={Link} to={`/surveillance-sheet/${s.id}/delete`}>
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

export default SurveillanceSheetList;
