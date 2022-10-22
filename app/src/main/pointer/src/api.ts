import axios from "axios";

import { Configuration, RoomApi } from "./generated/http-client";

const API_URL = location.origin;

const config = new Configuration({
  basePath: API_URL,
});

const axiosInstance = axios.create({
  baseURL: API_URL,
  withCredentials: true,
});

export const roomApi = new RoomApi(config, "", axiosInstance);
