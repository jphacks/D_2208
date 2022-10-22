import { AccessTokenResponse } from "@/generated/http-client";

/**
 * ログインユーザ
 */
export type AuthData = AccessTokenResponse & {
  userName: string;
  roomId: string;
};
