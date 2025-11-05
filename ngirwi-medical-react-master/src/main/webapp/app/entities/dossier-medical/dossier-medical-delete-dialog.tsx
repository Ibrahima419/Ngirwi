import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './dossier-medical.reducer';

export const DossierMedicalDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const dossierMedicalEntity = useAppSelector(state => state.dossierMedical.entity);
  const updateSuccess = useAppSelector(state => state.dossierMedical.updateSuccess);

  const handleClose = () => {
    navigate('/dossier-medical' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(dossierMedicalEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="dossierMedicalDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.dossierMedical.delete.question">
        Êtes-vous certain de vouloir supprimer le Dossier Medical {dossierMedicalEntity.id} ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-dossierMedical" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DossierMedicalDeleteDialog;
