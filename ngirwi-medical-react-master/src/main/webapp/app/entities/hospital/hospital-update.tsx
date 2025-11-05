import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHospital } from 'app/shared/model/hospital.model';
import { getEntity, updateEntity, createEntity, reset } from './hospital.reducer';

export const HospitalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hospitalEntity = useAppSelector(state => state.hospital.entity);
  const loading = useAppSelector(state => state.hospital.loading);
  const updating = useAppSelector(state => state.hospital.updating);
  const updateSuccess = useAppSelector(state => state.hospital.updateSuccess);

  const handleClose = () => {
    navigate('/hospital' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...hospitalEntity,
      ...values,
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
          ...hospitalEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ngirwiFrontEndApp.hospital.home.createOrEditLabel" data-cy="HospitalCreateUpdateHeading">
            Créer ou éditer un Hospital
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="hospital-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="hospital-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Adress" id="hospital-adress" name="adress" data-cy="adress" type="text" />
              <ValidatedField label="Phone" id="hospital-phone" name="phone" data-cy="phone" type="text" />
              <ValidatedBlobField label="Logo" id="hospital-logo" name="logo" data-cy="logo" isImage accept="image/*" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/hospital" replace color="info">
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

export default HospitalUpdate;
