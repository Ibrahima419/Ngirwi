import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './consultation.reducer';
import { TextFormat } from 'react-jhipster';

export const ConsultationDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const consultationEntity = useAppSelector(state => state.consultation.entity);
  const updateSuccess = useAppSelector(state => state.consultation.updateSuccess);

  const handleClose = () => {
    // navigate('/consultation' + location.search);
    window.history.back()
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(consultationEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="consultationDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.consultation.delete.question">
        Êtes-vous certain de vouloir supprimer la Consultation {consultationEntity.id} du <TextFormat type="date" value={consultationEntity.dateTime} format="DD/MM/YYYY" />?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-consultation" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ConsultationDeleteDialog;
