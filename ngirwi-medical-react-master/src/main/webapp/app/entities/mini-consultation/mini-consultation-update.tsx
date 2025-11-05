import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Form, FormFeedback, FormGroup, Input, Label, Row } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, updateEntity } from './mini-consultation.reducer';
import { IMiniConsultation } from 'app/shared/model/mini-consultation.model';
import { formatFcfa } from 'app/shared/util/entity-utils';

const MiniConsultationUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id, surveillanceSheetId } = useParams<'id' | 'surveillanceSheetId'>();
  const isNew = !id;

  const existing = useAppSelector(state => state.miniConsultation.entity);
  const [summary, setSummary] = useState('');
  const [diagnosis, setDiagnosis] = useState('');
  const [price, setPrice] = useState<string>('');
  const [errors, setErrors] = useState<{ summary?: string; price?: string }>({});

  useEffect(() => {
    if (!isNew && id) dispatch(getEntity(id));
  }, [id]);

  useEffect(() => {
    if (!isNew && existing?.id?.toString() === id) {
      setSummary(existing.summary || '');
      setDiagnosis(existing.diagnosis || '');
      setPrice(existing.price != null ? String(existing.price) : '');
    }
  }, [existing, id, isNew]);

  const validate = () => {
    const next: { summary?: string; price?: string } = {};
    if (!summary || summary.trim().length === 0) next.summary = 'Résumé requis';
    const p = Number(price);
    if (isNaN(p)) next.price = 'Prix requis';
    else if (p < 0) next.price = 'Le prix doit être >= 0';
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;
    const payload: IMiniConsultation = {
      id: isNew ? undefined : Number(id),
      summary: summary.trim(),
      diagnosis: diagnosis || undefined,
      price: Number(price),
      surveillanceSheetId: Number(surveillanceSheetId),
    };
    const action = isNew ? createEntity(payload) : updateEntity(payload);
    const res = await dispatch(action as any);
    if ((res as any).type?.endsWith('fulfilled')) {
      navigate(`/surveillance-sheets/${surveillanceSheetId}/mini-consultations`);
    }
  };

  return (
    <Row className="justify-content-center">
      <Col md="8">
        <h2>{isNew ? 'Nouvelle mini-consultation' : `Modifier mini-consultation #${id}`}</h2>
        <Form onSubmit={handleSubmit} noValidate>
          <FormGroup>
            <Label for="mc-summary">Résumé</Label>
            <Input
              id="mc-summary"
              type="textarea"
              value={summary}
              onChange={e => setSummary(e.target.value)}
              invalid={!!errors.summary}
              required
            />
            {errors.summary && <FormFeedback>{errors.summary}</FormFeedback>}
          </FormGroup>
          <FormGroup>
            <Label for="mc-diagnosis">Diagnostic</Label>
            <Input id="mc-diagnosis" type="textarea" value={diagnosis} onChange={e => setDiagnosis(e.target.value)} />
          </FormGroup>
          <FormGroup>
            <Label for="mc-price">Prix</Label>
            <Input
              id="mc-price"
              type="number"
              value={price}
              onChange={e => setPrice(e.target.value)}
              min={0}
              step={1}
              invalid={!!errors.price}
              required
            />
            <div className="mt-1">Aperçu: {formatFcfa(Number(price || 0))}</div>
            {errors.price && <FormFeedback>{errors.price}</FormFeedback>}
          </FormGroup>
          <div className="d-flex gap-2">
            <Button color="secondary" onClick={() => navigate(-1)}>
              Annuler
            </Button>
            <Button color="primary" type="submit">
              Enregistrer
            </Button>
          </div>
        </Form>
      </Col>
    </Row>
  );
};

export default MiniConsultationUpdate;
