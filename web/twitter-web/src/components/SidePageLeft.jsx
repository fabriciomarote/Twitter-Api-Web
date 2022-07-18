import React from 'react';
import { useNavigate } from "react-router-dom";
import '../styles/SidePageLeft.css';

  const SidePageLeft = (props) => {
    const {theme, setTheme } = props;

    const navigate = useNavigate();

    const goUser = () => { 
        navigate("/profile");
    }
  
    const goHome = () => {
        navigate("/home");
    }

    const goAddTweet = () => {
        navigate("/tweet");
    }

    const changeMode = () => {
        let colorTema;
        if (theme === 'light') {
            setTheme('dark');
            colorTema = 'dark';
        } else {
            setTheme('light');
            colorTema = 'light';
        }
        localStorage.setItem('theme', colorTema);
    }

    const tema = (theme === 'light') ? 'Claro' : 'Oscuro';

    var URLactual = window.location.href;

    return (
        <div className="side-left-container">
            <div className="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                <div className="side-left-button-content">
                    <form onSubmit={goHome}>
                        <button type="submit" className="btn-btn btn-info">
                            Inicio
                        </button>
                    </form>
                    <form onSubmit={goUser}>
                        <button type="submit" className="btn-btn btn-info">
                            Perfil
                        </button>
                    </form>  
                    <button className="btn-btn btn-info" onClick={() => changeMode()}>
                            Modo: {tema} 
                    </button> 
                    <form onSubmit={goAddTweet}>
                        <button type="submit" className="btn-btn btn-info">
                            Twittear
                        </button>
                    </form>         
                </div>    
            </div>
        </div>
    )
};

export default SidePageLeft;