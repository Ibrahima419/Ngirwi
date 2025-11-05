import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getPatient, deleteEntity as deleteDossier } from '../dossier-medical/dossier-medical.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './patient.reducer';

export const PatientDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getPatient(id))
    setLoadModal(true);
  }, []);

  const patientEntity = useAppSelector(state => state.patient.entity);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);
  const dossierEntity = useAppSelector(state => state.dossierMedical.entity);

  const handleClose = () => {
    navigate('/patient' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteDossier(dossierEntity.id));
    dispatch(deleteEntity(patientEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="patientDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.patient.delete.question">
        Êtes-vous certain de vouloir supprimer le Patient {patientEntity.lastName + ' ' + patientEntity.firstName} ?
        <br/> cni : {patientEntity.cni}
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-patient" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default PatientDeleteDialog;
