import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button, Card } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';
import { IoIosArrowBack } from 'react-icons/io';
import Header from 'app/shared/layout/header/header';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div 
      style={{
        paddingLeft:"16vw",
        paddingTop:"1%",
        fontFamily:"Mulish",
        fontWeight:"900",
        display:"flex",
        flexDirection:"column"
      }}
    >
          <Header pageName={"Administration"}/>
          <div
            style={{
              display:"flex",
              flexDirection:"column",
              gap:"5vh",
              marginTop:"9.5vh"
            }}
          >
            
            <Card
            style={{
              minHeight:"6.28vh",
              minWidth:"32vw",
              borderRadius:"20px",
              backgroundColor:"#11485C",
              textAlign:"center",
              color:"white",
              marginBottom:"2vh",
              marginLeft:"25vw",
              marginRight:"25vw",
              boxShadow:"0px 10px 50px rgba(138, 161, 203, 0.23)",
              display:"flex",
              flexDirection:"row",
              justifyContent:"center",
              alignItems:"center",
              paddingLeft:"1vw"
              }}
          >
            <Button replace onClick={() => window.history.back()} style={{color:"#53BFD1",backgroundColor:"#11485C",borderColor:"#11485C"}}>{React.createElement(IoIosArrowBack , {size:"20"})}</Button>
            <span>Modification mot de passe</span>
                     
          </Card>
            <Card 
              style={{
                minHeight:"70vh",
                marginRight:"5%",
                boxShadow:"0px 10px 50px rgba(138, 161, 203, 0.23)",
                borderRadius:"15px"

              }}
            >
            <span style={{marginTop:"1%", color:"#141414",fontSize:"20px", marginLeft:"3%"}}>Modification du mot de passe</span>
            <ValidatedForm id="password-form" onSubmit={handleValidSubmit}
                  style={{
                    width:"94%",
                    marginLeft:"3%",
                    height:"70%",
                    display:"flex",
                    flexDirection:"column",
                    justifyContent:"center",
                    marginTop:"2%",
                    fontSize:"15px",
                    fontWeight:"900",
                    alignItems:"center",
                    textAlign:"center"
                  }}
                >
               <ValidatedField
               style={{
                borderRadius:"25px",
                backgroundColor:"#F7FAFF",
                borderColor:"#CBDCF7",
                width:"75vw"
              }}
              name="currentPassword"
              label="Mot de passe actuel"
              placeholder="Mot de passe actuel..."
              type="password"
              validate={{
                required: { value: true, message: 'Votre mot de passe est requis.' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
            style={{
              borderRadius:"25px",
              backgroundColor:"#F7FAFF",
              borderColor:"#CBDCF7",
              width:"75vw"
            }}
              name="newPassword"
              label="Nouveau mot de passe"
              placeholder="Nouveau mot de passe..."
              type="password"
              validate={{
                required: { value: true, message: 'Votre mot de passe est requis.' },
                minLength: { value: 4, message: 'Votre mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre mot de passe ne doit pas comporter plus de 50 caractères.' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
            style={{
              borderRadius:"25px",
              backgroundColor:"#F7FAFF",
              borderColor:"#CBDCF7",
              width:"75vw"
            }}
              name="confirmPassword"
              label="Confirmation du nouveau mot de passe"
              placeholder="Confirmation du nouveau mot de passe..."
              type="password"
              validate={{
                required: { value: true, message: 'Votre confirmation du mot de passe est requise.' },
                minLength: { value: 4, message: 'Votre confirmation du mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre confirmation du mot de passe ne doit pas comporter plus de 50 caractères.' },
                validate: v => v === password || 'Le nouveau mot de passe et sa confirmation ne sont pas égaux !',
              }}
              data-cy="confirmPassword"
            />

              
                <Button 
                 style={{
                  borderRadius:"25px",
                  color:"white",
                  backgroundColor:"#56B5C5",
                  borderColor:"#56B5C5",
                  width:"60vw",textAlign:"center"
                }}
                color="success" type="submit" data-cy="submit">
                 Sauvegarder
              </Button>
                &nbsp;

                <Button onClick={()=>window.history.back()} id="cancel-save" data-cy="entityCreateCancelButton"  replace color="info"
                  style={{
                    borderRadius:"25px",
                    color:"white",
                    backgroundColor:"#EC4747",
                    borderColor:"#EC4747",
                    width:"60vw",textAlign:"center",marginBottom:"2vh"
                  }}
                >
                  <span className="d-none d-md-inline">Annuler</span>
                </Button>
              </ValidatedForm>
            </Card>
          </div>
    </div>
    // <div style={{marginLeft:"16vw"}}>
    //   <Row className="justify-content-center">
    //     <Col md="8">
    //       <h2 id="password-title">
    //         Changer le mot de passe pour [<strong>{account.login}</strong>]
    //       </h2>
    //       <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            // <ValidatedField
            //   name="currentPassword"
            //   label="Mot de passe actuel"
            //   placeholder="Mot de passe actuel"
            //   type="password"
            //   validate={{
            //     required: { value: true, message: 'Votre mot de passe est requis.' },
            //   }}
            //   data-cy="currentPassword"
            // />
            // <ValidatedField
            //   name="newPassword"
            //   label="Nouveau mot de passe"
            //   placeholder="Nouveau mot de passe"
            //   type="password"
            //   validate={{
            //     required: { value: true, message: 'Votre mot de passe est requis.' },
            //     minLength: { value: 4, message: 'Votre mot de passe doit comporter au moins 4 caractères.' },
            //     maxLength: { value: 50, message: 'Votre mot de passe ne doit pas comporter plus de 50 caractères.' },
            //   }}
            //   onChange={updatePassword}
            //   data-cy="newPassword"
            // />
            // <PasswordStrengthBar password={password} />
            // <ValidatedField
            //   name="confirmPassword"
            //   label="Confirmation du nouveau mot de passe"
            //   placeholder="Confirmation du nouveau mot de passe"
            //   type="password"
            //   validate={{
            //     required: { value: true, message: 'Votre confirmation du mot de passe est requise.' },
            //     minLength: { value: 4, message: 'Votre confirmation du mot de passe doit comporter au moins 4 caractères.' },
            //     maxLength: { value: 50, message: 'Votre confirmation du mot de passe ne doit pas comporter plus de 50 caractères.' },
            //     validate: v => v === password || 'Le nouveau mot de passe et sa confirmation ne sont pas égaux !',
            //   }}
            //   data-cy="confirmPassword"
            // />
            // <Button color="success" type="submit" data-cy="submit">
            //   Sauvegarder
            // </Button>
    //       </ValidatedForm>
    //     </Col>
    //   </Row>
    // </div>
  );
};

export default PasswordPage;
