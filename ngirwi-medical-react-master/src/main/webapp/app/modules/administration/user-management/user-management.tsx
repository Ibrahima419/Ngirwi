import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Badge, Card } from 'reactstrap';
import { TextFormat, JhiPagination, JhiItemCount, getSortState, ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getUsersAsAdmin, updateUser } from './user-management.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { RiUserAddLine } from 'react-icons/ri';
import Header from 'app/shared/layout/header/header';

export const UserManagement = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const getUsersFromProps = () => {
    dispatch(
      getUsersAsAdmin({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      })
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = user => () => {
    dispatch(
      updateUser({
        ...user,
        activated: !user.activated,
      })
    );
  };

  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);

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

      <Header pageName="Administration"/>

      <div
        style={{
          display:"flex",
          flexDirection:"column",
          position:"fixed",
          top:"15.5vh",

        }}          
      >
      
        <div
    style={{
      display: "grid",
      gridTemplateColumns: "repeat(3, 3fr)",
      alignItems: "center",
      columnGap: "5%",
      width: "75vw",
      marginLeft: "5%"
    }}
  >

<div
      style={{
        justifyContent: "center",
        alignItems: "center",
        minWidth: "15vw",
        minHeight: "15vw",
        borderRadius: "50%",
        backgroundColor: "#CBDCF7",
        paddingTop: "25%",
        paddingLeft: "4%",
        cursor:"pointer",
        display:"inline-block"
      }}
    >
      <span
        onClick={() => handleSyncList()}
        style={{
          display:"block",
          fontFamily: "Ubuntu",
          color: "#56B5C5",
          fontSize: "18px",
          textAlign: "center",
        }}
      >
        <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
      </span>
    </div>

    <Card
      style={{
        height: "6.28vh",
        width: "33.38vw",
        borderRadius: "20px",
        backgroundColor: "#11485C",
        textAlign: "center",
        color: "white",
        marginBottom: "5vh",
        boxShadow: "0px 10px 50px rgba(138, 161, 203, 0.23)",
      }}
    >
      <span style={{ marginTop: "1.5%" }}>Liste des comptes enregistrés</span>
    </Card>

    <Link to="/admin/user-management/new/"
              style={{
                display:"inline-block",
                textDecoration:"none",
                textAlign:"center", 
                color:"#56B5C5",
                minWidth:"15vw",
                minHeight:"15vw",
                borderRadius:"50%",
                backgroundColor:"#CBDCF7",
                fontSize:"18px",
                }}
              >
              <span style={{display:"block",marginTop:"20%"}}>
                {React.createElement( RiUserAddLine  ,{size: "24"})}   Enregistrer nouveau compte
              </span>
             
                           
            </Link>



  </div>


        <Card
          style={{
            width:"83vw",
            height:"70vh",
            backgroundColor:"white",
            position:"fixed",
            top:"32vh",
            marginRight:"1%",
            borderRadius:"15px 15px 0px 0px",
            boxShadow:"0px 2px 12px 4px rgba(138, 161, 203, 0.23)"


          }}
        >
          <span style={{marginTop:"1%", color:"#141414",fontSize:"20px", marginLeft:"3%", marginBottom:"1%"}}>Comptes enregistrés</span>

          <div 
            style={{
              display:"flex",
              flexDirection:"row",
              justifyContent:"flex-end",
              marginBottom:"3vh",
              marginRight:"2vw",
              gap:"1vw"
            }}
          >

          </div>
            <Table responsive
            
            >
          <thead 
           style={{
            position:"sticky",
            top:"0"
          }}
          >
          <tr>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('id')}>
              ID
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('login')}>
              Login
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('email')}>
              Email
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('activated')}
            >
              Statut
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            >Droits</th>
            <th
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('createdDate')}>
              Créé le
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            className="hand" onClick={sort('lastModifiedBy')}>
              Modifié par
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th 
            style={{
              textAlign:"center",
              fontSize:"14px",
              position:"sticky",
              top:"0",
              
              backgroundColor:"white",
            }}
            id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              Modifié le
              <FontAwesomeIcon style={{marginLeft:"10px"}} icon="sort" />
            </th>
            <th style={{
                textAlign:"center",
                fontSize:"14px",
                position:"sticky",
                top:"0",
                
                backgroundColor:"white",
              }} />
          </tr>
        </thead>
          <tbody 
          style={{
            backgroundColor:"#F6FAFF",
            border:"1px solid #F6FAFF",
            borderRadius:"15px 15px 0px 15px",
            fontSize:"15px",
            textAlign:"center",
            borderBottom:"50px solid white",
          }}
          >
          {users.map((user, i) => (
            <tr  style={{border:"1px solid #E9F1FF",borderRadius:"15px"}} id={user.login} key={`user-${i}`}>
              <td>
                <Button tag={Link} to={user.login} color="link" size="sm">
                  {user.id}
                </Button>
              </td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>
                {user.activated ? (
                  <Button color="success" onClick={toggleActive(user)}>
                    Activé
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(user)}>
                    Désactivé
                  </Button>
                )}
              </td>
              <td>
                {user.authorities
                  ? user.authorities.map((authority, j) => (
                      <div key={`user-auth-${i}-${j}`}>
                        <Badge color="info">{authority}</Badge>
                      </div>
                    ))
                  : null}
              </td>
              <td>
                {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
              </td>
              <td>{user.lastModifiedBy}</td>
              <td>
                {user.lastModifiedDate ? (
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td  
                className="text-end"
              >
                <div
                
                  style={{
                    display:"flex",
                    flexDirection:"row",
                    gap:"3px",
                    fontSize:"9px"
                  }} 
                  className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${user.login}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                  </Button>
                  <Button tag={Link} to={user.login} color="dark" size="sm">
                    <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                  </Button>
                  <Button tag={Link} to={`${user.login}/delete`} color="danger" size="sm" disabled={account.login === user.login}>
                    <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
            </Table>
          


        </Card>
      </div>

    
</div>
    // <div style={{marginLeft:"16vw"}}>
    //   <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
    //     Utilisateurs
    //     <div className="d-flex justify-content-end">
    //       <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
    //         <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
    //       </Button>
    //       <Link to="new" className="btn btn-primary jh-create-entity">
    //         <FontAwesomeIcon icon="plus" /> Créer un nouvel utilisateur
    //       </Link>
    //     </div>
    //   </h2>
    //   <Table responsive striped>
        // <thead>
        //   <tr>
        //     <th className="hand" onClick={sort('id')}>
        //       ID
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th className="hand" onClick={sort('login')}>
        //       Login
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th className="hand" onClick={sort('email')}>
        //       Email
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th />
        //     <th>Droits</th>
        //     <th className="hand" onClick={sort('createdDate')}>
        //       Créé le
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th className="hand" onClick={sort('lastModifiedBy')}>
        //       Modifié par
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
        //       Modifié le
        //       <FontAwesomeIcon icon="sort" />
        //     </th>
        //     <th />
        //   </tr>
        // </thead>
        // <tbody>
        //   {users.map((user, i) => (
        //     <tr id={user.login} key={`user-${i}`}>
        //       <td>
        //         <Button tag={Link} to={user.login} color="link" size="sm">
        //           {user.id}
        //         </Button>
        //       </td>
        //       <td>{user.login}</td>
        //       <td>{user.email}</td>
        //       <td>
        //         {user.activated ? (
        //           <Button color="success" onClick={toggleActive(user)}>
        //             Activé
        //           </Button>
        //         ) : (
        //           <Button color="danger" onClick={toggleActive(user)}>
        //             Désactivé
        //           </Button>
        //         )}
        //       </td>
        //       <td>
        //         {user.authorities
        //           ? user.authorities.map((authority, j) => (
        //               <div key={`user-auth-${i}-${j}`}>
        //                 <Badge color="info">{authority}</Badge>
        //               </div>
        //             ))
        //           : null}
        //       </td>
        //       <td>
        //         {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
        //       </td>
        //       <td>{user.lastModifiedBy}</td>
        //       <td>
        //         {user.lastModifiedDate ? (
        //           <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
        //         ) : null}
        //       </td>
        //       <td className="text-end">
        //         <div className="btn-group flex-btn-group-container">
        //           <Button tag={Link} to={user.login} color="info" size="sm">
        //             <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
        //           </Button>
        //           <Button tag={Link} to={`${user.login}/edit`} color="primary" size="sm">
        //             <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        //           </Button>
        //           <Button tag={Link} to={`${user.login}/delete`} color="danger" size="sm" disabled={account.login === user.login}>
        //             <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
        //           </Button>
        //         </div>
        //       </td>
        //     </tr>
        //   ))}
        // </tbody>
    //   </Table>
    //   {totalItems ? (
    //     <div className={users?.length > 0 ? '' : 'd-none'}>
    //       <div className="justify-content-center d-flex">
    //         <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
    //       </div>
    //       <div className="justify-content-center d-flex">
    //         <JhiPagination
    //           activePage={pagination.activePage}
    //           onSelect={handlePagination}
    //           maxButtons={5}
    //           itemsPerPage={pagination.itemsPerPage}
    //           totalItems={totalItems}
    //         />
    //       </div>
    //     </div>
    //   ) : (
    //     ''
    //   )}
    // </div>
  );
};

export default UserManagement;
