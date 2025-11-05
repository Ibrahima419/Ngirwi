import React, { useEffect, useLayoutEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import { FiLogOut } from 'react-icons/fi';
import { toast } from 'react-toastify';

export const LogoutDialog = () => {
  const dispatch = useAppDispatch();

    const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
    const updateSuccess = useAppSelector(state => state.consultation.updateSuccess);

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const handleClose = () => {
    window.history.back()
  };


  const confirmLogout = () => {
    window.location.href = "/logout"
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="LogoutDialogHeading">
        Confirmation déconnexion
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.logout.question">
        Êtes-vous certain de vouloir vous déconnecter ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-logout" data-cy="entityConfirmLogoutButton" color="danger" onClick={confirmLogout}>
          {/* <FontAwesomeIcon icon="trash" /> */} {React.createElement(FiLogOut, {size:"20"})}
          &nbsp; Déconnecter
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default LogoutDialog;
