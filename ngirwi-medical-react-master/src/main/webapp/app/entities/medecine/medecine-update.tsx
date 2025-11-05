import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrescription } from 'app/shared/model/prescription.model';
import { getEntities as getPrescriptions } from 'app/entities/prescription/prescription.reducer';
import { IMedecine } from 'app/shared/model/medecine.model';
import { getEntity, updateEntity, createEntity, reset } from './medecine.reducer';

export const MedecineUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const prescriptions = useAppSelector(state => state.prescription.entities);
  const medecineEntity = useAppSelector(state => state.medecine.entity);
  const loading = useAppSelector(state => state.medecine.loading);
  const updating = useAppSelector(state => state.medecine.updating);
  const updateSuccess = useAppSelector(state => state.medecine.updateSuccess);

  const handleClose = () => {
    navigate('/medecine' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPrescriptions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...medecineEntity,
      ...values,
      ordonance: prescriptions.find(it => it.id.toString() === values.ordonance.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...medecineEntity,
          ordonance: medecineEntity?.ordonance?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ngirwiFrontEndApp.medecine.home.createOrEditLabel" data-cy="MedecineCreateUpdateHeading">
            Créer ou éditer un Medecine
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="medecine-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="medecine-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Duration" id="medecine-duration" name="duration" data-cy="duration" type="text" />
              <ValidatedField label="Frequency" id="medecine-frequency" name="frequency" data-cy="frequency" type="text" />
              <ValidatedField id="medecine-ordonance" name="ordonance" data-cy="ordonance" label="Ordonance" type="select">
                <option value="" key="0" />
                {prescriptions
                  ? prescriptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/medecine" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Retour</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Sauvegarder
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MedecineUpdate;
