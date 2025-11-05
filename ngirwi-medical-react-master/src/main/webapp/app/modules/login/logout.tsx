import React, { useLayoutEffect, useState } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import { Navigate, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import ReactRedirect from "react-redirect"

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [red, setRed] = useState(false);

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      // window.location.href = '/';
      toast("Déconnexion")
      setRed(true)
    }
  });
 
  return (
    
  
      <Navigate to="/login"/>
    
    
    
  );
};

export default Logout;
