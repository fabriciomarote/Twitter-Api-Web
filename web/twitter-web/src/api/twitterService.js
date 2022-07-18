import axios from 'axios';

var TwitterService = {
    getUser: function() { return axios.get('http://localhost:8080/user')},
    getUserById: function(id) { return axios.get(`http://localhost:8080/user/${id}`)},
    getTweetById: function(id) { return axios.get(`http://localhost:8080/tweet/${id}`)},
    postEditProfile: function(data) { return axios.post('http://localhost:8080/user', data)},
    postLogin: function(data) { return axios.post('http://localhost:8080/login', data)},
    deleteTweet: function(id) { return axios.delete(`http://localhost:8080/tweet/${id}`)},
    postRegister: function(data) { return axios.post('http://localhost:8080/register', data)},
    putLike: function(id) { return axios.put(`http://localhost:8080/tweet/${id}/like`)},
    postComment: function(id, data) { return axios.post(`http://localhost:8080/tweet/${id}/comment`, data)},
    putByFollow: function(id) {return axios.put(`http://localhost:8080/user/${id}/follow`)},
    addTweet: function(data) {return axios.post('http://localhost:8080/tweet', data)},
    getSearch: function(data) {return axios.get(`http://localhost:8080/search?q=${data}`)},
};

export default TwitterService;