import './header.scss';

import React, { useState } from 'react';

import { Navbar, Nav, NavbarToggler, Collapse } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from './header-components';
import { AdminMenu, EntitiesMenu, AccountMenu } from '../menus';
import { Link } from 'react-router-dom';
import { FiLogOut } from 'react-icons/fi';
import { ValidatedField } from 'react-jhipster';
import { useAppSelector } from 'app/config/store';
import { AiOutlineSearch } from 'react-icons/ai';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}

const Header = (props) => {



  const patientLists = useAppSelector(state => state.patient.entities);
  const [query, setQuery] = useState("")

  

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div style={{display:"flex", flexDirection:"row" ,fontFamily:"Jost", fontSize:"20px" , zIndex:"1", position:"absolute", top:"2"}}>
    <span style={{width:"62vw"}}>{props.pageName}</span>  
    <div style={{display:"flex", flexDirection:"row", alignItems:"flex-start", gap:"2vw"}}> 
  <div style={{display:"flex", flexDirection:"column"}}>
  
  <span style={{position:"absolute", top:"3%",marginLeft:"1%",color:"#C9CED6"}}>{React.createElement(AiOutlineSearch, {size : '18'} )}</span> 
   <ValidatedField name='search'  placeholder="   Rechercher..." onChange={event =>setQuery(event.target.value)} 
      style={{
              backgroundColor:"#F9F9FB",
              fontFamily:"Mulish",
              fontWeight:"900",
              borderRadius:"12px",
              height:"5vh",
              width:"15vw",
              borderColor:"#F9F9FB",
              fontSize:"13px",
              paddingLeft:"30px",
              boxShadow:"0px 4px 11px rgba(0, 0, 0, 0.25)"
            }} 
    />
      {  
      patientLists.filter(post => {
      if (query === "") {
      
        post = "Recherche de patients..."
        return post;
      } else if (post.lastName.toLowerCase().includes(query.toLowerCase())) {
       
        return post;
      }else if (post.firstName.toLowerCase().includes(query.toLowerCase())) {
       
        return post;
      }else if (post.cni.toLowerCase().includes(query.toLowerCase())) {
       
        return post;
      }else if (post.phone.toLowerCase().includes(query.toLowerCase())) {
       
        return post;
      }
    }).map((patient, i)=>

      <Link hidden={query===""?true:false} key={patient.id} to={`/patient/${patient.id}`} 
      style={{
        backgroundColor:"white",
        margin:"0px",
        position:"sticky",
        top:"0",
        borderColor:"#F9F9FB",
        color:"#11485C",
        borderRadius:"3px",
        textDecoration:"none",
        height:"5vh",
        fontSize:"13px"}}>{patient.firstName+' '+patient.lastName}</Link>
    


           )

      }
    
  </div>

      <Link to="/logoutDialog" style={{color:"silver", fontWeight:"1800"}}>
        <div>{React.createElement(FiLogOut)} </div>
      </Link>
    </div>
  </div>
  );
};

export default Header;
