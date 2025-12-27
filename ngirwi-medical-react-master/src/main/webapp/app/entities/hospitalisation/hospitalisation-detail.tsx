import React, { useEffect, useMemo, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import {
  Button,
  Row,
  Col,
  Card,
  CardBody,
  CardHeader,
  Badge,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Input,
  Label,
  FormGroup,
} from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { formatFcfa } from 'app/shared/util/entity-utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity as getHospitalisation } from './hospitalisation.reducer';
import { getByHospitalisation, reset, selectSheetsByHospitalisation } from '../surveillance-sheet/surveillance-sheet.reducer';
import { getEntity as getPatient } from '../patient/patient.reducer';

export const HospitalisationDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const hospitalisationEntity = useAppSelector(state => state.hospitalisation?.entity);
  const sheets = useAppSelector(state => selectSheetsByHospitalisation(state, Number(id))) || [];
  const patient = useAppSelector(state => state.patient?.entity);

  const [mcTotal, setMcTotal] = useState<number>(0);
  const [isMcModalOpen, setMcModalOpen] = useState(false);
  const [mcSheetId, setMcSheetId] = useState<number | ''>('');
  const [mcSummary, setMcSummary] = useState('');
  const [mcDiagnosis, setMcDiagnosis] = useState('');
  const [mcPrice, setMcPrice] = useState<number | ''>('');
  const toggleMcModal = () => setMcModalOpen(x => !x);

  // Close hospitalisation modal state
  const [isCloseModalOpen, setCloseModalOpen] = useState(false);
  const [closeDate, setCloseDate] = useState<string>(() => new Date().toISOString().slice(0, 16)); // datetime-local value
  const [closeDiagnosis, setCloseDiagnosis] = useState<string>('');
  const [closeGenerateBill, setCloseGenerateBill] = useState<boolean>(false);
  const toggleCloseModal = () => setCloseModalOpen(x => !x);
  const [closeDateError, setCloseDateError] = useState<string>('');

  // Mini-consultation inline validation errors
  const [mcPriceError, setMcPriceError] = useState<string>('');
  const [mcSheetError, setMcSheetError] = useState<string>('');

  // Mini-consultations flattened list for this hospitalisation
  const [miniList, setMiniList] = useState<any[]>([]);
  const [miniLoading, setMiniLoading] = useState<boolean>(false);

  useEffect(() => {
    if (id) {
      dispatch(reset());
      dispatch(getHospitalisation(id));
      dispatch(getByHospitalisation({ hospitalisationId: Number(id) }));
    }
  }, [id]);

  useEffect(() => {
    const pid = hospitalisationEntity?.patient?.id || hospitalisationEntity?.patientId;
    if (pid) {
      if (!patient?.id || Number(patient.id) !== Number(pid)) dispatch(getPatient(pid));
    }
  }, [hospitalisationEntity?.patientId, hospitalisationEntity?.patient, patient?.id]);

  // Aggregate mini-consultations total across all sheets
  useEffect(() => {
    const list = Array.isArray(sheets) ? sheets : [];
    if (!list.length) {
      setMcTotal(0);
      return;
    }
    (async () => {
      try {
        const totals = await Promise.all(
          list.map(async (s: any) => {
            const sid = s.id;
            if (!sid) return 0;
            const { data } = await axios.get<any[]>(`api/mini-consultations/by-surveillance/${sid}`);
            return (data || []).reduce((acc: number, mc: any) => acc + Number(mc.price || 0), 0);
          })
        );
        setMcTotal(totals.reduce((a, b) => a + b, 0));
      } catch (e) {
        setMcTotal(0);
      }
    })();
  }, [JSON.stringify(sheets.map((s: any) => s.id))]);

  // Load mini-consultations across all sheets and flatten into a single list
  useEffect(() => {
    const list = Array.isArray(sheets) ? sheets : [];
    let cancelled = false;
    if (!list.length) {
      setMiniList([]);
      return undefined;
    }
    setMiniLoading(true);
    (async () => {
      try {
        const results = await Promise.all(
          list.map(async (s: any) => {
            const sid = s?.id;
            if (!sid) return [] as any[];
            const { data } = await axios.get<any[]>(`api/mini-consultations/by-surveillance/${sid}`);
            const sheetDate = s?.sheetDate;
            return (data || []).map(m => ({
              ...m,
              _sheetId: sid,
              _sheetDate: m?.surveillanceSheet?.sheetDate || sheetDate || null,
            }));
          })
        );
        const flat = results.flat();
        if (!cancelled) setMiniList(flat);
      } catch (e) {
        if (!cancelled) setMiniList([]);
      } finally {
        if (!cancelled) setMiniLoading(false);
      }
    })();
    return () => {
      cancelled = true;
      setMiniList([]);
    };
  }, [JSON.stringify(sheets.map((s: any) => s.id)), hospitalisationEntity?.id]);


  // useEffect(() => {
  //   document.body.style.overflow = 'hidden';
  //   document.documentElement.style.overflow = 'hidden';

  //   return () => {
  //     document.body.style.overflow = 'auto';
  //     document.documentElement.style.overflow = 'auto';
  //   };
  // }, []);

  const stat = useMemo(() => {
    const list = Array.isArray(sheets) ? sheets : [];
    const meds = list.reduce((acc: number, s: any) => acc + ((s.medications || []).length || 0), 0);
    const acts = list.reduce((acc: number, s: any) => acc + ((s.acts || []).length || 0), 0);
    const cost = list.reduce((acc: number, s: any) => {
      const medsTotal = (s.medications || []).reduce(
        (a: number, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)),
        0
      );
      const actsTotal = (s.acts || []).reduce(
        (a: number, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)),
        0
      );
      const fallback = Number(s.total || s.totalCost || 0);
      const sheetCost = medsTotal + actsTotal;
      return acc + (sheetCost || fallback);
    }, 0);
    return { count: list.length, meds, acts, cost, mcTotal, combined: cost + mcTotal };
  }, [sheets, mcTotal]);

  const status = (hospitalisationEntity?.status || '').toString();
  const isClosed = status === 'DONE' || status === 'CLOSED' || status === 'FERMEE' || status === 'CLOTUREE';

  const submitMiniConsultation = async () => {
    try {
      setMcSheetError('');
      setMcPriceError('');
      if (!mcSheetId) {
        setMcSheetError('Veuillez sélectionner une fiche.');
        return;
      }
      if (mcPrice === '' || isNaN(Number(mcPrice)) || Number(mcPrice) < 0) {
        setMcPriceError('Prix invalide (≥ 0).');
        return;
      }
      await axios.post('api/mini-consultations', {
        surveillanceSheetId: Number(mcSheetId),
        summary: mcSummary || '',
        diagnosis: mcDiagnosis || '',
        price: Number(mcPrice),
      });
      // Refresh totals
      setMcModalOpen(false);
      setMcSummary('');
      setMcDiagnosis('');
      setMcPrice('');
      setMcSheetId('');
      // Trigger totals recompute
      const list = Array.isArray(sheets) ? sheets : [];
      const totals = await Promise.all(
        list.map(async (s: any) => {
          const sid = s.id;
          if (!sid) return 0;
          const { data } = await axios.get<any[]>(`api/mini-consultations/by-surveillance/${sid}`);
          return (data || []).reduce((acc: number, mc: any) => acc + Number(mc.price || 0), 0);
        })
      );
      setMcTotal(totals.reduce((a, b) => a + b, 0));
    } catch (e) {
      // swallow for now; could show toast
    }
  };

  const submitCloseHospitalisation = async () => {
    try {
      if (!id) return;
      // Convert datetime-local to ISO for backend Instant
      const closeInstant = new Date(closeDate);
      setCloseDateError('');
      if (hospitalisationEntity?.entryDate) {
        const entryInstant = new Date(hospitalisationEntity.entryDate);
        if (closeInstant.getTime() < entryInstant.getTime()) {
          setCloseDateError("La date de sortie ne peut pas être avant la date d'entrée.");
          return;
        }
      }
      const iso = closeInstant.toISOString();
      await axios.post(`/api/hospitalisations/${id}/close`, null, {
        params: {
          releaseDate: iso,
          finalDiagnosis: closeDiagnosis || undefined,
          generateBill: closeGenerateBill,
        },
      });
      setCloseModalOpen(false);
      // Refresh entity
      dispatch(getHospitalisation(id));
    } catch (e) {
      // could add a toast; keep silent for now
    }
  };

  const authorities = useAppSelector(state => state.authentication.account.authorities || []);
  const isAdminOrDoctor =
  authorities.includes('ROLE_ADMIN') || authorities.includes('ROLE_DOCTOR');


  const handleExportPdf = async () => {
    if (!id) return;
    try {
      // Assumed export endpoint (update here if your backend path differs)
      const url = `/api/hospitalisations/${id}/resume.pdf`;
      const { data } = await axios.get(url, {
        responseType: 'blob',
        headers: { Accept: 'application/pdf' },
      });
      const blob = new Blob([data], { type: 'application/pdf' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `hospitalisation-${id}.pdf`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      URL.revokeObjectURL(link.href);
    } catch (e) {
      // could show toast
    }
  };

  return (
    <div
      className="text-dark"
      style={{ paddingLeft: '16vw', paddingTop: '1%', fontFamily: 'Mulish', fontWeight: 900, display: 'flex', flexDirection: 'column' }}
    >
      {/* Header */}
      <div className="d-flex align-items-center justify-content-between mb-3">
        <div>
          <h2 className="mb-1">
            Hospitalisation #{hospitalisationEntity?.id}{' '}
            {status && (
              <Badge color={isClosed ? 'secondary' : 'primary'} className="ms-2">
                {isClosed ? 'Clôturée' : 'EN COURS'}
              </Badge>
            )}
          </h2>
          <div className="text-muted">
            {patient?.lastName && patient?.firstName
              ? `${(patient.lastName || '').toUpperCase()} ${patient.firstName || ''}`
              : hospitalisationEntity?.patient?.fullName || ''}
          </div>
        </div>
        <div className="d-flex gap-2">
          {isAdminOrDoctor && (
            <Button color="danger" className="px-3" onClick={toggleCloseModal} disabled={isClosed}>
              <FontAwesomeIcon icon="check" className="me-1" /> Clôturer
            </Button>
          )}
        </div>
      </div>

      <Row className="g-3">
        {/* Main content */}
        <Col md="8">
          <Card className="mb-3 shadow-sm">
            <CardHeader className="fw-bold">Informations du patient</CardHeader>
            <CardBody>
              <Row className="gy-2">
                <Col md="6">
                  <div className="small text-muted">Nom complet</div>
                  <div className="fw-semibold">
                    {patient?.lastName && patient?.firstName
                      ? `${(patient.lastName || '').toUpperCase()} ${patient.firstName || ''}`
                      : hospitalisationEntity?.patient?.fullName || '—'}
                  </div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Numéro patient</div>
                  <div className="fw-semibold">
                    <span className="me-1" style={{ color: '#ff4d4f' }}>
                      •
                    </span>
                    P{patient?.id || hospitalisationEntity?.patientId || '—'}
                  </div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Date de naissance</div>
                  <div className="fw-semibold">
                    {patient?.birthday ? <TextFormat type="date" value={patient.birthday} format={APP_DATE_FORMAT} /> : '—'}
                  </div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Téléphone</div>
                  <div className="fw-semibold">{patient?.phone || '—'}</div>
                </Col>
                <Col md="12">
                  <div className="small text-muted">Adresse</div>
                  <div className="fw-semibold">{patient?.adress || '—'}</div>
                </Col>
              </Row>
            </CardBody>
          </Card>

          <Card className="mb-3 shadow-sm">
            <CardHeader className="fw-bold">Détails de l’hospitalisation</CardHeader>
            <CardBody>
              <Row className="gy-2">
                <Col md="6">
                  <div className="small text-muted">Service</div>
                  <div className="fw-semibold">{hospitalisationEntity?.service || '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Médecin responsable</div>
                  <div className="fw-semibold">{hospitalisationEntity?.doctorName || '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Date d’entrée</div>
                  <div className="fw-semibold">
                    {hospitalisationEntity?.entryDate ? (
                      <TextFormat value={hospitalisationEntity.entryDate} type="date" format={APP_DATE_FORMAT} />
                    ) : (
                      '—'
                    )}
                  </div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Motif d’admission</div>
                  <div className="fw-semibold">{hospitalisationEntity?.admissionReason || '—'}</div>
                </Col>
                <Col md="12">
                  <div className="small text-muted">Observations</div>
                  <div className="fw-semibold" style={{ whiteSpace: 'pre-wrap' }}>
                    {hospitalisationEntity?.entryDiagnosis || hospitalisationEntity?.observations || '—'}
                  </div>
                </Col>
              </Row>
            </CardBody>
          </Card>

          <div className="d-flex align-items-center justify-content-between mb-2">
            <h5 className="mb-0">Fiches de surveillance</h5>
            {isAdminOrDoctor && (
              <Button
                color="success"
                onClick={() => navigate(`/surveillance-sheet/new?hospitalisationId=${hospitalisationEntity?.id}`)}
                disabled={isClosed}
              >
                <FontAwesomeIcon icon="plus" /> &nbsp; Ajouter une fiche
              </Button>
            )}
          </div>
          <div className="d-flex flex-column gap-2">
            {(Array.isArray(sheets) ? sheets : []).map((s: any) => (
              <Card key={s.id} className="shadow-sm">
                <CardBody className="d-flex align-items-center justify-content-between">
                  <div className="d-flex flex-column">
                    <div className="fw-semibold">Date de la fiche: {s.sheetDate ? s.sheetDate : '—'}</div>
                    <div className="text-muted small">
                      Médicaments: {(s.medications || []).length} &nbsp; • &nbsp; Actes: {(s.acts || []).length}
                    </div>
                    {s.notes && <div className="small mt-1">{s.notes}</div>}
                  </div>
                  <div className="d-flex align-items-center gap-3">
                    <div className="fw-bold" style={{ color: '#ff6600' }}>
                      {formatFcfa(
                        (() => {
                          const medsTotal = (s.medications || []).reduce(
                            (a: number, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)),
                            0
                          );
                          const actsTotal = (s.acts || []).reduce(
                            (a: number, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)),
                            0
                          );
                          const fallback = Number(s.total || s.totalCost || 0);
                          return medsTotal + actsTotal || fallback;
                        })()
                      )}
                    </div>
                    {isAdminOrDoctor && (
                    <Button color="secondary" outline onClick={() => navigate(`/surveillance-sheet/${s.id}`)}>
                      Voir
                    </Button>
                    )}
                  </div>
                </CardBody>
              </Card>
            ))}
            {(!sheets || (Array.isArray(sheets) && sheets.length === 0)) && <div className="text-muted">Aucune fiche de surveillance.</div>}
          </div>

          {/* Mini-consultations section */}
          <div className="d-flex align-items-center justify-content-between mt-4 mb-2">
            <h5 className="mb-0">Mini-consultations</h5>
            {isAdminOrDoctor && (
              <Button
                color="primary"
                outline
                onClick={toggleMcModal}
                disabled={isClosed || !Array.isArray(sheets) || (sheets as any[]).length === 0}
              >
                <FontAwesomeIcon icon="plus" /> &nbsp; Ajouter une mini-consultation
              </Button>
            )}
          </div>
          <div className="d-flex flex-column gap-2">
            {miniLoading && <div className="text-muted small">Chargement…</div>}
            {!miniLoading && miniList.length > 0 && (
              <Card className="shadow-sm">
                <CardBody className="py-2">
                  {miniList.map((m: any, i: number) => (
                    <div
                      key={`mc-row-${m.id || i}`}
                      className="d-flex flex-wrap justify-content-between align-items-center py-1 small border-bottom"
                      style={{ gap: 8 }}
                    >
                      <div className="d-flex flex-column" style={{ minWidth: 240 }}>
                        <div className="fw-semibold">{m.summary || m.diagnosis || `Mini-consultation ${m.id}`}</div>
                        <div className="text-muted">
                          {m._sheetDate ? `${m._sheetDate}` : ''}
                          {m._sheetId ? (
                            <>
                              {m._sheetDate ? ' · ' : ''}
                              <Link to={`/surveillance-sheet/${m._sheetId}`}>Fiche #{m._sheetId}</Link>
                            </>
                          ) : null}
                        </div>
                      </div>
                      <div className="d-flex align-items-center" style={{ minWidth: 160 }}>
                        <div className="fw-bold me-2" style={{ color: '#ff6600' }}>
                          {formatFcfa(Number(m.price || 0))}
                        </div>
                        {m._sheetId && isAdminOrDoctor && (
                          <Button
                            size="sm"
                            outline
                            color="secondary"
                            tag={Link}
                            to={`/surveillance-sheet/${m._sheetId}`}
                            className="ms-auto"
                          >
                            Voir
                          </Button>
                        )}
                      </div>
                    </div>
                  ))}
                </CardBody>
              </Card>
            )}
            {!miniLoading && miniList.length === 0 && <div className="text-muted">Aucune mini-consultation.</div>}
          </div>
        </Col>

        {/* Right Sidebar */}
        <Col md="4">
          <Card className="mb-3 shadow-sm">
            <CardHeader className="fw-bold">Coût total</CardHeader>
            <CardBody>
              <div className="display-6 fw-bold" style={{ color: '#ff6600' }}>
                {formatFcfa(hospitalisationEntity?.totalAmount || 0)}
              </div>
            </CardBody>
          </Card>

          <Card className="mb-3 shadow-sm">
            <CardHeader className="fw-bold">Statistiques</CardHeader>
            <CardBody>
              <div className="d-flex justify-content-between">
                <span>Fiches créées</span>
                <span className="fw-semibold">{stat.count}</span>
              </div>
              <div className="d-flex justify-content-between">
                <span>Médicaments total</span>
                <span className="fw-semibold">{stat.meds}</span>
              </div>
              <div className="d-flex justify-content-between">
                <span>Actes total</span>
                <span className="fw-semibold">{stat.acts}</span>
              </div>
              <hr className="my-2" />
              <div className="d-flex justify-content-between">
                <span>Coût mini-consultations</span>
                <span className="fw-semibold">{formatFcfa(stat.mcTotal)}</span>
              </div>
              <div className="d-flex justify-content-between">
                <span>Coût fiches + mini-consultations</span>
                <span className="fw-semibold">{formatFcfa(stat.combined)}</span>
              </div>
            </CardBody>
          </Card>

          <Card className="mb-3 shadow-sm">
            <CardHeader className="fw-bold">Actions</CardHeader>
            <CardBody className="d-flex flex-column gap-2">
              {isAdminOrDoctor && (
                <Button color="primary" outline onClick={() => navigate(`/hospitalisation/${hospitalisationEntity?.id}/edit`)}>
                  <FontAwesomeIcon icon="pencil-alt" className="me-2" /> Modifier les informations
                </Button>
              )}
              {isAdminOrDoctor && (
              <Button color="secondary" outline onClick={() => window.print()}>
                <FontAwesomeIcon icon="print" className="me-2" /> Imprimer le dossier
              </Button>
              )}
              {isAdminOrDoctor && (
              <Button color="secondary" outline disabled={!isClosed} onClick={handleExportPdf}>
                <FontAwesomeIcon icon="file-pdf" className="me-2" /> Exporter PDF
              </Button>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Mini-consultation modal */}
      <Modal isOpen={isMcModalOpen} toggle={toggleMcModal} backdrop>
        <ModalHeader toggle={toggleMcModal}>Ajouter une mini-consultation</ModalHeader>
        <ModalBody>
          <FormGroup>
            <Label for="mc-sheet">Fiche de surveillance</Label>
            <Input
              id="mc-sheet"
              type="select"
              value={mcSheetId as any}
              onChange={e => setMcSheetId(e.target.value ? Number(e.target.value) : '')}
            >
              <option value="">— Sélectionner —</option>
              {(Array.isArray(sheets) ? sheets : []).map((s: any) => (
                <option key={s.id} value={s.id}>
                  {s.sheetDate || `Fiche ${s.id}`}
                </option>
              ))}
            </Input>
            {mcSheetError && <small className="text-danger">{mcSheetError}</small>}
          </FormGroup>
          <FormGroup className="mt-2">
            <Label for="mc-summary">Résumé</Label>
            <Input id="mc-summary" type="text" value={mcSummary} onChange={e => setMcSummary(e.target.value)} />
          </FormGroup>
          <FormGroup>
            <Label for="mc-diagnosis">Diagnostic</Label>
            <Input id="mc-diagnosis" type="text" value={mcDiagnosis} onChange={e => setMcDiagnosis(e.target.value)} />
          </FormGroup>
          <FormGroup>
            <Label for="mc-price">Prix</Label>
            <Input
              id="mc-price"
              type="number"
              value={mcPrice as any}
              onChange={e => setMcPrice(e.target.value === '' ? '' : Number(e.target.value))}
              min={0}
            />
            {mcPriceError && <small className="text-danger">{mcPriceError}</small>}
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={submitMiniConsultation} disabled={!mcSheetId || mcPrice === ''}>
            Enregistrer
          </Button>
          <Button color="secondary" outline onClick={toggleMcModal}>
            Annuler
          </Button>
        </ModalFooter>
      </Modal>

      {/* Close hospitalisation modal */}
      <Modal isOpen={isCloseModalOpen} toggle={toggleCloseModal} backdrop>
        <ModalHeader toggle={toggleCloseModal}>Clôturer l’hospitalisation</ModalHeader>
        <ModalBody>
          <FormGroup>
            <Label for="close-date">Date de sortie</Label>
            <Input id="close-date" type="datetime-local" value={closeDate} onChange={e => setCloseDate(e.target.value)} />
            {closeDateError && <small className="text-danger">{closeDateError}</small>}
          </FormGroup>
          <FormGroup>
            <Label for="close-dx">Diagnostic final</Label>
            <Input id="close-dx" type="text" value={closeDiagnosis} onChange={e => setCloseDiagnosis(e.target.value)} />
          </FormGroup>
          <FormGroup check>
            <Input id="close-bill" type="checkbox" checked={closeGenerateBill} onChange={e => setCloseGenerateBill(e.target.checked)} />
            <Label for="close-bill" check>
              Générer la facture
            </Label>
          </FormGroup>
        </ModalBody>
        <ModalFooter>
          <Button color="danger" onClick={submitCloseHospitalisation}>
            Confirmer la clôture
          </Button>
          <Button color="secondary" outline onClick={toggleCloseModal}>
            Annuler
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

// Minimal inline component to render mini-consultations per sheet
const MiniConsultationsForSheet = ({ sheet }: { sheet: any }) => {
  const [items, setItems] = React.useState<any[] | null>(
    (sheet && Array.isArray(sheet.miniConsultations) ? sheet.miniConsultations : null) as any[] | null
  );

  React.useEffect(() => {
    let mounted = true;
    const sid = sheet?.id;
    if (!sid) return;
    if (items && items.length >= 0) return; // already set (even empty array)
    (async () => {
      try {
        const { data } = await axios.get<any[]>(`api/mini-consultations/by-surveillance/${sid}`);
        if (mounted) setItems(Array.isArray(data) ? data : []);
      } catch (e) {
        if (mounted) setItems([]);
      }
    })();
    return () => {
      mounted = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [sheet?.id]);

  const list = Array.isArray(items)
    ? items
    : (sheet && Array.isArray(sheet.miniConsultations) ? (sheet.miniConsultations as any[]) : []).slice();

  if (!list.length) return null;

  return (
    <Card className="shadow-sm">
      <CardBody>
        {list.map((mc: any) => (
          <div key={mc.id} className="d-flex justify-content-between small py-1">
            <span>
              {/* associated record: show sheet reference and diagnosis */}
              <Link to={`/surveillance-sheet/${sheet?.id}`} className="text-decoration-none">
                Fiche #{sheet?.id}
              </Link>
              {sheet?.sheetDate ? ` · ${sheet.sheetDate}` : ''} — {mc.diagnosis || ''}
            </span>
            <span className="fw-semibold">{formatFcfa(Number(mc.price || 0))}</span>
          </div>
        ))}
      </CardBody>
    </Card>
  );
};

export default HospitalisationDetail;
