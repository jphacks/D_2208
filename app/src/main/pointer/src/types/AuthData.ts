import { AccessToken } from "@/gql/graphql";

/**
 * ログインユーザ
 */
export type AuthData = AccessToken & {
  userName: string;
  roomId: string;
};
