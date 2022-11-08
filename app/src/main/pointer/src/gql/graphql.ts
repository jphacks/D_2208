/* eslint-disable */
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

export type AccessTokenResponse = {
  __typename?: 'AccessTokenResponse';
  accessToken?: Maybe<Scalars['String']>;
  tokenType?: Maybe<Scalars['String']>;
  ttl?: Maybe<Scalars['Int']>;
};

export type Mutation = {
  __typename?: 'Mutation';
  createRoom?: Maybe<RoomResponse>;
  deleteRoom: Scalars['ID'];
  joinRoom?: Maybe<AccessTokenResponse>;
};


export type MutationDeleteRoomArgs = {
  roomId: Scalars['ID'];
};


export type MutationJoinRoomArgs = {
  passcode?: InputMaybe<Scalars['String']>;
  roomId: Scalars['ID'];
  userName?: InputMaybe<Scalars['String']>;
};

export type Query = {
  __typename?: 'Query';
  health?: Maybe<Scalars['Boolean']>;
};

export type RoomResponse = {
  __typename?: 'RoomResponse';
  passcode?: Maybe<Scalars['String']>;
  roomId: Scalars['ID'];
};

export type Subscription = {
  __typename?: 'Subscription';
  getUsers?: Maybe<Array<Maybe<UserResponse>>>;
};


export type SubscriptionGetUsersArgs = {
  roomId: Scalars['ID'];
};

export type UserResponse = {
  __typename?: 'UserResponse';
  id: Scalars['ID'];
  name?: Maybe<Scalars['String']>;
  roomId?: Maybe<Scalars['String']>;
};
