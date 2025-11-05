import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Row, Col, Alert, Button, Form } from 'reactstrap';
import { toast } from 'react-toastify';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';
import { BrandIcon } from 'app/shared/layout/header/header-components';

export const RegisterPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: 'en' }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div style={{display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"flex-start",height:"100vh"}}>
    <div>
        <BrandIcon style={{ width:"140px", height:"137px"}}/>
    </div>
    <div style={{ height:"65vh",width:"50vw", backgroundColor:"white", boxShadow:"0px 10px 50px rgba(138, 161, 203, 0.23)"}}>
      <div style={{display:"flex",alignItems:"center",flexDirection:"column",marginTop:"3%"}}>
        <h3 style={{color:"#54BFD0", fontFamily:"Mulish", fontWeight:"900"}}>NGIRWI MEDICAL</h3>
        <h3 style={{color:"#11142D", fontFamily:"Mulish", fontWeight:"700"}}>Inscription</h3>
      </div>
      <div style={{borderColor:"#54BFD0",backgroundColor:"#54BFD0",height:"3.75px",position:"relative", top:"2%"}}></div>
      <div style={{marginTop:"5%" }}> 
        <ValidatedForm style={{display:"grid", gridTemplateColumns : "repeat(2, 5fr)",columnGap:"5%",rowGap:"2%",paddingLeft:"6%",paddingRight:"6%"}} onSubmit={handleValidSubmit}>
            <ValidatedField 
           name="username"
              label="Nom d'utilisateur"
              placeholder="Nom d'utilisateur"
              validate={{
                required: { value: true, message:"Nom d'utilisateur obligatoire." },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: "Votre nom d'utilisateur est invalide.",
                },
                minLength: { value: 1, message: "Votre nom d'utilisateur doit contenir plus d'un caractère." },
                maxLength: { value: 50, message: "Votre nom d'utilisateur ne peut pas contenir plus de 50 caractères." },
              }}
              data-cy="username"
            />

           <ValidatedField
              name="email"
              label="Email"
              placeholder="Email"
              type="email"
              validate={{
                required: { value: true, message: 'Email requis.' },
                minLength: { value: 5, message: 'Votre email doit comporter au moins 5 caractères.' },
                maxLength: { value: 254, message: 'Votre email ne doit pas comporter plus de 50 caractères.' },
                validate: v => isEmail(v) || "Votre email n'est pas valide.",
              }}
              data-cy="email"
            />

         <ValidatedField
              name="firstPassword"
              label="Mot de passe"
              placeholder="Mot de passe"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'Mot de passe requis.' },
                minLength: { value: 4, message: 'Votre mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre mot de passe ne doit pas comporter plus de 50 caractères.' },
              }}
              data-cy="firstPassword"
            />

         <ValidatedField
              name="secondPassword"
              label="Confirmer mot de passe"
              placeholder="Confirmer mot de passe"
              type="password"
              validate={{
                required: { value: true, message: 'Confirmation requise.' },
                minLength: { value: 4, message: 'Votre confirmation du mot de passe doit comporter au moins 4 caractères.' },
                maxLength: { value: 50, message: 'Votre confirmation du mot de passe ne doit pas comporter plus de 50 caractères.' },
                validate: v => v === password || 'Le nouveau mot de passe et sa confirmation ne sont pas égaux !',
              }}
              data-cy="secondPassword"
            />

          <Button variant="primary" type="submit" data-cy="submit" className="btn btn-lg" style={{gridColumn:"1/3"  ,backgroundColor:"#54BFD0", fontFamily:"Mulish", fontWeight:"900", borderColor:"#54BFD0", fontSize:"15px", position:"relative", bottom:"3%"}}>
            Je m'insccris
          </Button>

        </ValidatedForm>
      </div>
    </div>
  </div>
    // <div>
    //   <Row className="justify-content-center">
    //     <Col md="8">
    //       <h1 id="register-title" data-cy="registerTitle">
    //         Création de compte utilisateur
    //       </h1>
    //     </Col>
    //   </Row>
    //   <Row className="justify-content-center">
    //     <Col md="8">
    //       <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
    //         <ValidatedField
    //           name="username"
    //           label="Nom d'utilisateur"
    //           placeholder="Votre nom d'utilisateur"
    //           validate={{
    //             required: { value: true, message: "Votre nom d'utilisateur est obligatoire." },
    //             pattern: {
    //               value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
    //               message: "Votre nom d'utilisateur est invalide.",
    //             },
    //             minLength: { value: 1, message: "Votre nom d'utilisateur doit contenir plus d'un caractère." },
    //             maxLength: { value: 50, message: "Votre nom d'utilisateur ne peut pas contenir plus de 50 caractères." },
    //           }}
    //           data-cy="username"
    //         />
    //         <ValidatedField
    //           name="email"
    //           label="Email"
    //           placeholder="Votre email"
    //           type="email"
    //           validate={{
    //             required: { value: true, message: 'Votre email est requis.' },
    //             minLength: { value: 5, message: 'Votre email doit comporter au moins 5 caractères.' },
    //             maxLength: { value: 254, message: 'Votre email ne doit pas comporter plus de 50 caractères.' },
    //             validate: v => isEmail(v) || "Votre email n'est pas valide.",
    //           }}
    //           data-cy="email"
    //         />
    //         <ValidatedField
    //           name="firstPassword"
    //           label="Nouveau mot de passe"
    //           placeholder="Nouveau mot de passe"
    //           type="password"
    //           onChange={updatePassword}
    //           validate={{
    //             required: { value: true, message: 'Votre mot de passe est requis.' },
    //             minLength: { value: 4, message: 'Votre mot de passe doit comporter au moins 4 caractères.' },
    //             maxLength: { value: 50, message: 'Votre mot de passe ne doit pas comporter plus de 50 caractères.' },
    //           }}
    //           data-cy="firstPassword"
    //         />
    //         <PasswordStrengthBar password={password} />
    //         <ValidatedField
    //           name="secondPassword"
    //           label="Confirmation du nouveau mot de passe"
    //           placeholder="Confirmation du nouveau mot de passe"
    //           type="password"
    //           validate={{
    //             required: { value: true, message: 'Votre confirmation du mot de passe est requise.' },
    //             minLength: { value: 4, message: 'Votre confirmation du mot de passe doit comporter au moins 4 caractères.' },
    //             maxLength: { value: 50, message: 'Votre confirmation du mot de passe ne doit pas comporter plus de 50 caractères.' },
    //             validate: v => v === password || 'Le nouveau mot de passe et sa confirmation ne sont pas égaux !',
    //           }}
    //           data-cy="secondPassword"
    //         />
    //         <Button id="register-submit" color="primary" type="submit" data-cy="submit">
    //           Enregistrement
    //         </Button>
    //       </ValidatedForm>
    //       <p>&nbsp;</p>
    //       <Alert color="warning">
    //         <span>Si vous voulez vous</span>
    //         <a className="alert-link">connecter</a>
    //         <span>
    //           , vous pouvez utiliser les comptes par défaut : <br /> - Administrateur (nom d&apos;utilisateur=&quot;admin&quot; et mot de
    //           passe =&quot;admin&quot;) <br /> - Utilisateur (nom d&apos;utilisateur=&quot;user&quot; et mot de passe =&quot;user&quot;).
    //         </span>
    //       </Alert>
    //     </Col>
    //   </Row>
    // </div>
  );
};

export default RegisterPage;
