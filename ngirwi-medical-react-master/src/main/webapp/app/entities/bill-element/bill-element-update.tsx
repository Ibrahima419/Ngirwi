import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBill } from 'app/shared/model/bill.model';
import { getEntities as getBills } from 'app/entities/bill/bill.reducer';
import { IBillElement } from 'app/shared/model/bill-element.model';
import { getEntity, updateEntity, createEntity, reset } from './bill-element.reducer';

export const BillElementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bills = useAppSelector(state => state.bill.entities);
  const billElementEntity = useAppSelector(state => state.billElement.entity);
  const loading = useAppSelector(state => state.billElement.loading);
  const updating = useAppSelector(state => state.billElement.updating);
  const updateSuccess = useAppSelector(state => state.billElement.updateSuccess);

  const handleClose = () => {
    navigate('/bill-element' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBills({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...billElementEntity,
      ...values,
      bill: bills.find(it => it.id.toString() === values.bill.toString()),
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
          ...billElementEntity,
          bill: billElementEntity?.bill?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ngirwiFrontEndApp.billElement.home.createOrEditLabel" data-cy="BillElementCreateUpdateHeading">
            Créer ou éditer un Bill Element
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bill-element-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="bill-element-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Price" id="bill-element-price" name="price" data-cy="price" type="text" />
              <ValidatedField label="Percentage" id="bill-element-percentage" name="percentage" data-cy="percentage" type="text" />
              <ValidatedField label="Quantity" id="bill-element-quantity" name="quantity" data-cy="quantity" type="text" />
              <ValidatedField id="bill-element-bill" name="bill" data-cy="bill" label="Bill" type="select">
                <option value="" key="0" />
                {bills
                  ? bills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bill-element" replace color="info">
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

export default BillElementUpdate;
