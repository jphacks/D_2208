/* eslint-disable */
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
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

/** アクセストークン */
export type AccessToken = {
  __typename?: 'AccessToken';
  /** アクセストークン */
  accessToken: Scalars['String'];
  /** トークンタイプ */
  tokenType: Scalars['String'];
  /** TTL [s] */
  ttl: Scalars['Int'];
  /** ユーザ */
  user: User;
};

/** カスタムポインター */
export type CustomPointer = {
  __typename?: 'CustomPointer';
  /** カスタムポインターID */
  id: Scalars['ID'];
  /** ラベル */
  label: Scalars['String'];
  /** URL */
  url: Scalars['String'];
};

/** カスタムポインターリスト */
export type CustomPointers = {
  __typename?: 'CustomPointers';
  /** カスタムポインターリスト */
  customPointers: Array<CustomPointer>;
};

export type Mutation = {
  __typename?: 'Mutation';
  /** ポインタータイプ変更API */
  changePointerType: Scalars['String'];
  /** カスタムポインター作成API */
  createCustomPointer: Scalars['ID'];
  /** ルーム作成API */
  createRoom: Room;
  /** カスタムポインター削除API */
  deleteCustomPointer: Scalars['ID'];
  /** ルーム削除API */
  deleteRoom: Scalars['ID'];
  /** ポインター切断API */
  disconnectPointer: User;
  /** スライドを進めるAPI */
  goNextSlide: SlideControl;
  /** スライドを戻すAPI */
  goPreviousSlide: SlideControl;
  /** ルーム入室API */
  joinRoom: AccessToken;
  /** ポインター操作API */
  movePointer: PointerControl;
  /** タイマー一時停止API */
  pauseTimer: Timer;
  /** タイマーリセットAPI */
  resetTimer: Timer;
  /** タイマー再開API */
  resumeTimer: Timer;
  /** タイマー開始API */
  startTimer: Timer;
};


export type MutationChangePointerTypeArgs = {
  pointerType: Scalars['String'];
  roomId: Scalars['ID'];
};


export type MutationCreateCustomPointerArgs = {
  content: Scalars['String'];
  id: Scalars['ID'];
  label: Scalars['String'];
  roomId: Scalars['ID'];
};


export type MutationCreateRoomArgs = {
  pointerType: Scalars['String'];
};


export type MutationDeleteCustomPointerArgs = {
  id: Scalars['ID'];
  roomId: Scalars['ID'];
};


export type MutationDeleteRoomArgs = {
  roomId: Scalars['ID'];
};


export type MutationDisconnectPointerArgs = {
  accessToken: Scalars['String'];
};


export type MutationGoNextSlideArgs = {
  accessToken: Scalars['String'];
};


export type MutationGoPreviousSlideArgs = {
  accessToken: Scalars['String'];
};


export type MutationJoinRoomArgs = {
  passcode: Scalars['String'];
  roomId: Scalars['ID'];
  userName: Scalars['String'];
};


export type MutationMovePointerArgs = {
  accessToken: Scalars['String'];
  alpha?: InputMaybe<Scalars['Float']>;
  beta?: InputMaybe<Scalars['Float']>;
  gamma?: InputMaybe<Scalars['Float']>;
};


export type MutationPauseTimerArgs = {
  accessToken: Scalars['String'];
};


export type MutationResetTimerArgs = {
  accessToken: Scalars['String'];
};


export type MutationResumeTimerArgs = {
  accessToken: Scalars['String'];
};


export type MutationStartTimerArgs = {
  accessToken: Scalars['String'];
  inputTime: Scalars['Int'];
};

/** ポインター操作 */
export type PointerControl = {
  __typename?: 'PointerControl';
  /** 操作方向 */
  orientation: PointerControlOrientation;
  /** 操作者 */
  user: User;
};

/** ポインター操作方向 */
export type PointerControlOrientation = {
  __typename?: 'PointerControlOrientation';
  /** α値 */
  alpha: Scalars['Float'];
  /** β値 */
  beta: Scalars['Float'];
  /** γ値 */
  gamma: Scalars['Float'];
};

export type Query = {
  __typename?: 'Query';
  /** カスタムポインターリスト取得API */
  getCustomPointers: CustomPointers;
  /** ポインタータイプ取得API */
  getPointerType: Scalars['String'];
  /** タイマー取得API */
  getTimer: Timer;
  /** ユーザリスト取得API */
  getUsers: Users;
  /** ヘルスチェックAPI */
  health: Scalars['Boolean'];
};


export type QueryGetCustomPointersArgs = {
  roomId: Scalars['ID'];
};


export type QueryGetPointerTypeArgs = {
  roomId: Scalars['ID'];
};


export type QueryGetTimerArgs = {
  roomId: Scalars['ID'];
};


export type QueryGetUsersArgs = {
  roomId: Scalars['ID'];
};

/** ルーム */
export type Room = {
  __typename?: 'Room';
  /** ルームID */
  id: Scalars['ID'];
  /** パスコード */
  passcode: Scalars['String'];
  /** ポインタータイプ */
  pointerType: Scalars['String'];
};

/** スライド操作 */
export enum SlideControl {
  /** 進める */
  Next = 'NEXT',
  /** 戻す */
  Previous = 'PREVIOUS'
}

export type Subscription = {
  __typename?: 'Subscription';
  /** カスタムポインターリスト購読API */
  subscribeToCustomPointers: CustomPointers;
  /** ポインター操作購読API */
  subscribeToPointer: PointerControl;
  /** ポインター切断イベント購読API */
  subscribeToPointerDisconnectEvent: User;
  /** ポインタータイプ購読API */
  subscribeToPointerType: Scalars['String'];
  /** ルーム終了イベント購読API */
  subscribeToRoomFinishEvent: Scalars['ID'];
  /** スライド操作購読API */
  subscribeToSlideControl: SlideControl;
  /** タイマー購読API */
  subscribeToTimer: Timer;
  /** ユーザリスト購読API */
  subscribeToUsers: Users;
};


export type SubscriptionSubscribeToCustomPointersArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToPointerArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToPointerDisconnectEventArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToPointerTypeArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToRoomFinishEventArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToSlideControlArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToTimerArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToUsersArgs = {
  roomId: Scalars['ID'];
};

/** タイマー */
export type Timer = {
  __typename?: 'Timer';
  /** 終了時刻 */
  finishAt: Scalars['String'];
  /** 入力時間 [s] */
  inputTime: Scalars['Int'];
  /** 一時停止時点での残り時間 [s] */
  remainingTimeAtPaused?: Maybe<Scalars['Int']>;
  /** ステータス */
  status: TimerStatus;
};

/** タイマーステータス */
export enum TimerStatus {
  /** 一時停止中 */
  Paused = 'PAUSED',
  /** 準備中 */
  Ready = 'READY',
  /** 実行中 */
  Running = 'RUNNING'
}

/** ユーザ */
export type User = {
  __typename?: 'User';
  /** ユーザID */
  id: Scalars['ID'];
  /** ルーム名 */
  name: Scalars['String'];
};

/** ユーザリスト */
export type Users = {
  __typename?: 'Users';
  /** ユーザリスト */
  users: Array<User>;
};

export type CreateRoomMutationVariables = Exact<{ [key: string]: never; }>;


export type CreateRoomMutation = { __typename?: 'Mutation', createRoom: { __typename?: 'Room', id: string, passcode: string } };

export type SubscribeToSlideControlSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToSlideControlSubscription = { __typename?: 'Subscription', subscribeToSlideControl: SlideControl };

export type SubscribeToPointerSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToPointerSubscription = { __typename?: 'Subscription', subscribeToPointer: { __typename?: 'PointerControl', orientation: { __typename?: 'PointerControlOrientation', alpha: number, beta: number, gamma: number }, user: { __typename?: 'User', id: string, name: string } } };

export type SubscribeToPointerDisconnectEventSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToPointerDisconnectEventSubscription = { __typename?: 'Subscription', subscribeToPointerDisconnectEvent: { __typename?: 'User', id: string } };

export type SubscribeToPointerTypeSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToPointerTypeSubscription = { __typename?: 'Subscription', subscribeToPointerType: string };

export type SubscribeToUsersSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToUsersSubscription = { __typename?: 'Subscription', subscribeToUsers: { __typename?: 'Users', users: Array<{ __typename?: 'User', id: string, name: string }> } };

export type DeleteRoomMutationVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type DeleteRoomMutation = { __typename?: 'Mutation', deleteRoom: string };

export type ChangePointerTypeMutationVariables = Exact<{
  pointerType: Scalars['String'];
  roomId: Scalars['ID'];
}>;


export type ChangePointerTypeMutation = { __typename?: 'Mutation', changePointerType: string };

export type GetUsersQueryVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type GetUsersQuery = { __typename?: 'Query', getUsers: { __typename?: 'Users', users: Array<{ __typename?: 'User', id: string, name: string }> } };


export const CreateRoomDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"CreateRoom"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"createRoom"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"pointerType"},"value":{"kind":"StringValue","value":"SPOTLIGHT","block":false}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"passcode"}}]}}]}}]} as unknown as DocumentNode<CreateRoomMutation, CreateRoomMutationVariables>;
export const SubscribeToSlideControlDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToSlideControl"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToSlideControl"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<SubscribeToSlideControlSubscription, SubscribeToSlideControlSubscriptionVariables>;
export const SubscribeToPointerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToPointer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToPointer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"orientation"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"alpha"}},{"kind":"Field","name":{"kind":"Name","value":"beta"}},{"kind":"Field","name":{"kind":"Name","value":"gamma"}}]}},{"kind":"Field","name":{"kind":"Name","value":"user"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]} as unknown as DocumentNode<SubscribeToPointerSubscription, SubscribeToPointerSubscriptionVariables>;
export const SubscribeToPointerDisconnectEventDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToPointerDisconnectEvent"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToPointerDisconnectEvent"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]} as unknown as DocumentNode<SubscribeToPointerDisconnectEventSubscription, SubscribeToPointerDisconnectEventSubscriptionVariables>;
export const SubscribeToPointerTypeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToPointerType"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToPointerType"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<SubscribeToPointerTypeSubscription, SubscribeToPointerTypeSubscriptionVariables>;
export const SubscribeToUsersDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToUsers"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToUsers"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"users"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]} as unknown as DocumentNode<SubscribeToUsersSubscription, SubscribeToUsersSubscriptionVariables>;
export const DeleteRoomDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DeleteRoom"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"deleteRoom"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<DeleteRoomMutation, DeleteRoomMutationVariables>;
export const ChangePointerTypeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"ChangePointerType"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pointerType"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"changePointerType"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"pointerType"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pointerType"}}},{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<ChangePointerTypeMutation, ChangePointerTypeMutationVariables>;
export const GetUsersDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetUsers"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getUsers"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"users"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"name"}}]}}]}}]}}]} as unknown as DocumentNode<GetUsersQuery, GetUsersQueryVariables>;