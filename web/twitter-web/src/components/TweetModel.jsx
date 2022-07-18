import React, { useState } from 'react';
import service from '../api/twitterService'
import { Link } from "react-router-dom";
import Themes from '../theme/Themes'
import { ThemeProvider } from 'styled-components';
import { TweetModelContainer } from '../theme/Changes';
import '../styles/Tweets.css';

const TweetModel = ({ id, text, images, reply, likes, date, author, comments, uName }) => {
  const [tw, setTw] = useState({
    images: images,
    like: likes,
    comments: comments,
    reply: reply,
  });

  const [theme, setTheme] = useState(localStorage.getItem('theme'));

  const addLike = (id) => {
    service.putLike(id)
    service.getTweetById(id)
      .then(response =>
        setTw((prevState) => ({
          ...prevState,
          like: response.data.likes,
        }))
      )
  };

  const iconColor = (theme === 'light') ? 'ccurrentColor' : 'white';

  return (
    <ThemeProvider theme={Themes[theme]}>
      <TweetModelContainer>
    <div className="tweetContent" key={`tweet-${id}`}>
      <div className="tweet-data-content">
        <div className="image-tweet-content">
          <Link to={`/user/${author.id}`} className="link-tweet">
            <img className="image-author-tweet" alt="imagen de autor" src={author.image} width="0" height="50"/>
          </Link>
        </div>
        <div className="name-tweet-content">
          <Link to={`/user/${author.id}`} className="link-tweet">
            <div className="nameTweet-container">
              <p className="name-tweet">{author.name}</p>
            </div>
            <div className="idTweet-container">
              <p className="id-tweet">@{author.id}</p>
            </div>
          </Link>
        </div>
      </div>  
      <Link to={`/tweet/${id}`} className="link-tweet">
        <p className="textTweet">{text}</p>
      </Link>
      <div className="images-tweet-container" id="asd">
        <div className="mid-right">
          {tw.images.map(image => (
            <img key={String(image)} className="image-tweet" src={image} alt="imagen de tweet" />
          ))}
        </div>
      </div>
      <div className="date-Tweet">
        <p className="dateTweet">{date}</p>
      </div>
      <div className="icons-content">
        <div className="icon-comments">
        <Link to={`/tweet/${id}`} className="link-tweet">
          <button type="submit" className="button-icon comment-btn">
          {tw.comments.find(u => u.author.name === uName)
            ?
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="Deepskyblue" className="bi bi-chat-fill" viewBox="0 0 16 16">
              <path d="M8 15c4.418 0 8-3.134 8-7s-3.582-7-8-7-8 3.134-8 7c0 1.76.743 3.37 1.97 4.6-.097 1.016-.417 2.13-.771 2.966-.079.186.074.394.273.362 2.256-.37 3.597-.938 4.18-1.234A9.06 9.06 0 0 0 8 15z" />
            </svg>
            :
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill={iconColor} className="bi bi-chat" viewBox="0 0 16 16">
              <path d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z" />
            </svg>}     {tw.comments.length}
          </button>
        </Link>
        </div>  
        <div className="icon-like"> 
          <button type="submit" className="button-icon like-btn"
            onClick={(event) => { event.preventDefault(); addLike(id) }}>{tw.like.find(u => u.name === uName)
              ?
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="Red" className="bi bi-heart-fill" viewBox="0 0 16 16">
                <path fillRule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z" />
              </svg>
              :
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill={iconColor} className="bi bi-heart" viewBox="0 0 16 16">
                <path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01L8 2.748zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143c.06.055.119.112.176.171a3.12 3.12 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z" />
              </svg>}      {tw.like.length}
          </button>
        </div> 
      </div>
    </div>
    </TweetModelContainer>
    </ThemeProvider>
  )
}

export default TweetModel;