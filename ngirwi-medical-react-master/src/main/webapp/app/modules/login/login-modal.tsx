import React from 'react';
import { ValidatedField } from 'react-jhipster';
import { Button,  Form } from 'reactstrap';
import { useForm } from 'react-hook-form';

import { BrandIcon } from 'app/shared/layout/header/header-components';
import { Link } from 'react-router-dom';

export interface ILoginModalProps {
  loginError: boolean;
  handleLogin: (username: string, password: string) => void;
}

const LoginModal = (props: ILoginModalProps) => {
  const login = ({ username, password}) => {
    props.handleLogin(username, password);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const { loginError} = props;

  const handleLoginSubmit = e => {
    handleSubmit(login)(e);
  };

  return (
    <div style={{display:"flex",flexDirection:"column",alignItems:"center",justifyContent:"flex-start",height:"100vh"}}>
    <div>
        <BrandIcon style={{ width:"140px", height:"137px"}}/>
    </div>
    <div style={{ height:"70vh",width:"50vw", backgroundColor:"white", boxShadow:"0px 10px 50px rgba(138, 161, 203, 0.23)"}}>
      <div style={{display:"flex",alignItems:"center",flexDirection:"column",marginTop:"3%"}}>
        <h3 style={{color:"#54BFD0", fontFamily:"Mulish", fontWeight:"900"}}>NGIRWI MEDICAL</h3>
        <h3 style={{color:"#11142D", fontFamily:"Mulish", fontWeight:"700"}}>Connexion</h3>
      </div>
      <div style={{borderColor:"#54BFD0",backgroundColor:"#54BFD0",height:"3.75px",position:"relative", top:"2%"}}></div>
      <div style={{marginTop:"3%" }}> 
        <Form style={{display:"flex", flexDirection:"column",gap:"25%",paddingLeft:"15%",paddingRight:"15%"}} onSubmit={handleLoginSubmit}>
            <ValidatedField 
              name="username"
              label="Nom d'utilisateur"
              placeholder="saisissez votre nom d'utilisteur"
              required
              autoFocus
              data-cy="username"
              validate={{ required: "Veuillez saisir un nom d'utilisateur !" }}
              register={register}
              error={errors.username}
              isTouched={touchedFields.username}
            />

            <ValidatedField 
              name="password"
              type="password"
              label="Mot de passe"
              placeholder="saisissez votre mot de passe"
              required
              data-cy="password"
              validate={{ required: 'Veuillez saisir votre mot de passe!' }}
              register={register}
              error={errors.password}
              isTouched={touchedFields.password}
              
            />

            {/* <div>
              <Link 
                to="/account/reset/request" 
                style={{
                  fontSize:"12px",
                  color:"GrayText"
                }}
              >
                Mot de passe oublié?
              </Link>
            </div> */}

             {loginError ? (
                <div style={{color:"red"}} data-cy="loginError">
                  <strong>Erreur d&apos;authentification !</strong> Veuillez vérifier vos identifiants de connexion.
                </div>
              ) : null}
          <Button variant="primary" type="submit" data-cy="submit" className="btn btn-lg" style={{marginTop:"1%",  backgroundColor:"#54BFD0", fontFamily:"Mulish", fontWeight:"900", borderColor:"#54BFD0", fontSize:"15px"}}>
            Se connecter
          </Button>
         <span style={{position:"relative", left:"47%", color:"#54BFD0"}}>ou</span> 
          <Button href="/account/reset/request" variant="primary" type="submit" className="btn btn-lg"  style={{ backgroundColor:"white", color:"#54BFD0", fontFamily:"Mulish", fontWeight:"900", borderColor:"#54BFD0", fontSize:"15px"}}>
          Mot de passe oublié?
          </Button>

        </Form>
      </div>
    </div>
  </div>
  );
};

export default LoginModal;
