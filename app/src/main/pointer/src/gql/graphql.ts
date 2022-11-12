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

export type SubscribeToRoomFinishEventSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToRoomFinishEventSubscription = { __typename?: 'Subscription', subscribeToRoomFinishEvent: string };

export type JoinRoomMutationVariables = Exact<{
  roomId: Scalars['ID'];
  passcode: Scalars['String'];
  userName: Scalars['String'];
}>;


export type JoinRoomMutation = { __typename?: 'Mutation', joinRoom: { __typename?: 'AccessToken', tokenType: string, accessToken: string, ttl: number, user: { __typename?: 'User', id: string } } };

export type GoNextSlideMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type GoNextSlideMutation = { __typename?: 'Mutation', goNextSlide: SlideControl };

export type GoPreviousSlideMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type GoPreviousSlideMutation = { __typename?: 'Mutation', goPreviousSlide: SlideControl };

export type GetPointerTypeQueryVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type GetPointerTypeQuery = { __typename?: 'Query', getPointerType: string };

export type SubscribeToPointerTypeSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToPointerTypeSubscription = { __typename?: 'Subscription', subscribeToPointerType: string };

export type GetCustomPointersQueryVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type GetCustomPointersQuery = { __typename?: 'Query', getCustomPointers: { __typename?: 'CustomPointers', customPointers: Array<{ __typename?: 'CustomPointer', id: string, label: string, url: string }> } };

export type SubscribeToCustomPointersSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeToCustomPointersSubscription = { __typename?: 'Subscription', subscribeToCustomPointers: { __typename?: 'CustomPointers', customPointers: Array<{ __typename?: 'CustomPointer', id: string, label: string, url: string }> } };

export type ChangePointerTypeMutationVariables = Exact<{
  roomId: Scalars['ID'];
  pointerType: Scalars['String'];
}>;


export type ChangePointerTypeMutation = { __typename?: 'Mutation', changePointerType: string };

export type StartTimerMutationVariables = Exact<{
  inputTime: Scalars['Int'];
  accessToken: Scalars['String'];
}>;


export type StartTimerMutation = { __typename?: 'Mutation', startTimer: { __typename?: 'Timer', status: TimerStatus } };

export type ResumeTimeMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type ResumeTimeMutation = { __typename?: 'Mutation', resumeTimer: { __typename?: 'Timer', status: TimerStatus } };

export type PauseTimerMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type PauseTimerMutation = { __typename?: 'Mutation', pauseTimer: { __typename?: 'Timer', status: TimerStatus } };

export type ResetTimerMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type ResetTimerMutation = { __typename?: 'Mutation', resetTimer: { __typename?: 'Timer', status: TimerStatus } };

export type SubscribeTimerSubscriptionVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type SubscribeTimerSubscription = { __typename?: 'Subscription', subscribeToTimer: { __typename?: 'Timer', status: TimerStatus, inputTime: number, remainingTimeAtPaused?: number | null, finishAt: string } };

export type GetTimerQueryVariables = Exact<{
  roomId: Scalars['ID'];
}>;


export type GetTimerQuery = { __typename?: 'Query', getTimer: { __typename?: 'Timer', status: TimerStatus, inputTime: number, remainingTimeAtPaused?: number | null, finishAt: string } };

export type MovePointerMutationVariables = Exact<{
  alpha?: InputMaybe<Scalars['Float']>;
  beta?: InputMaybe<Scalars['Float']>;
  gamma?: InputMaybe<Scalars['Float']>;
  accessToken: Scalars['String'];
}>;


export type MovePointerMutation = { __typename?: 'Mutation', movePointer: { __typename?: 'PointerControl', user: { __typename?: 'User', id: string } } };

export type DisconnectPointerMutationVariables = Exact<{
  accessToken: Scalars['String'];
}>;


export type DisconnectPointerMutation = { __typename?: 'Mutation', disconnectPointer: { __typename?: 'User', id: string } };


export const SubscribeToRoomFinishEventDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToRoomFinishEvent"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToRoomFinishEvent"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<SubscribeToRoomFinishEventSubscription, SubscribeToRoomFinishEventSubscriptionVariables>;
export const JoinRoomDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"JoinRoom"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"passcode"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"userName"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"joinRoom"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}},{"kind":"Argument","name":{"kind":"Name","value":"passcode"},"value":{"kind":"Variable","name":{"kind":"Name","value":"passcode"}}},{"kind":"Argument","name":{"kind":"Name","value":"userName"},"value":{"kind":"Variable","name":{"kind":"Name","value":"userName"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"tokenType"}},{"kind":"Field","name":{"kind":"Name","value":"accessToken"}},{"kind":"Field","name":{"kind":"Name","value":"ttl"}},{"kind":"Field","name":{"kind":"Name","value":"user"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]}}]} as unknown as DocumentNode<JoinRoomMutation, JoinRoomMutationVariables>;
export const GoNextSlideDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"GoNextSlide"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"goNextSlide"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}]}]}}]} as unknown as DocumentNode<GoNextSlideMutation, GoNextSlideMutationVariables>;
export const GoPreviousSlideDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"GoPreviousSlide"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"goPreviousSlide"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}]}]}}]} as unknown as DocumentNode<GoPreviousSlideMutation, GoPreviousSlideMutationVariables>;
export const GetPointerTypeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetPointerType"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getPointerType"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<GetPointerTypeQuery, GetPointerTypeQueryVariables>;
export const SubscribeToPointerTypeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToPointerType"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToPointerType"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}]}]}}]} as unknown as DocumentNode<SubscribeToPointerTypeSubscription, SubscribeToPointerTypeSubscriptionVariables>;
export const GetCustomPointersDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetCustomPointers"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getCustomPointers"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"customPointers"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"label"}},{"kind":"Field","name":{"kind":"Name","value":"url"}}]}}]}}]}}]} as unknown as DocumentNode<GetCustomPointersQuery, GetCustomPointersQueryVariables>;
export const SubscribeToCustomPointersDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeToCustomPointers"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToCustomPointers"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"customPointers"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"label"}},{"kind":"Field","name":{"kind":"Name","value":"url"}}]}}]}}]}}]} as unknown as DocumentNode<SubscribeToCustomPointersSubscription, SubscribeToCustomPointersSubscriptionVariables>;
export const ChangePointerTypeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"ChangePointerType"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"pointerType"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"changePointerType"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}},{"kind":"Argument","name":{"kind":"Name","value":"pointerType"},"value":{"kind":"Variable","name":{"kind":"Name","value":"pointerType"}}}]}]}}]} as unknown as DocumentNode<ChangePointerTypeMutation, ChangePointerTypeMutationVariables>;
export const StartTimerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"StartTimer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"inputTime"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"Int"}}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"startTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"inputTime"},"value":{"kind":"Variable","name":{"kind":"Name","value":"inputTime"}}},{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}}]}}]}}]} as unknown as DocumentNode<StartTimerMutation, StartTimerMutationVariables>;
export const ResumeTimeDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"ResumeTime"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"resumeTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}}]}}]}}]} as unknown as DocumentNode<ResumeTimeMutation, ResumeTimeMutationVariables>;
export const PauseTimerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"PauseTimer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"pauseTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}}]}}]}}]} as unknown as DocumentNode<PauseTimerMutation, PauseTimerMutationVariables>;
export const ResetTimerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"ResetTimer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"resetTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}}]}}]}}]} as unknown as DocumentNode<ResetTimerMutation, ResetTimerMutationVariables>;
export const SubscribeTimerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"subscription","name":{"kind":"Name","value":"SubscribeTimer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"subscribeToTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}},{"kind":"Field","name":{"kind":"Name","value":"inputTime"}},{"kind":"Field","name":{"kind":"Name","value":"remainingTimeAtPaused"}},{"kind":"Field","name":{"kind":"Name","value":"finishAt"}}]}}]}}]} as unknown as DocumentNode<SubscribeTimerSubscription, SubscribeTimerSubscriptionVariables>;
export const GetTimerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"GetTimer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"ID"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"getTimer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"roomId"},"value":{"kind":"Variable","name":{"kind":"Name","value":"roomId"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"status"}},{"kind":"Field","name":{"kind":"Name","value":"inputTime"}},{"kind":"Field","name":{"kind":"Name","value":"remainingTimeAtPaused"}},{"kind":"Field","name":{"kind":"Name","value":"finishAt"}}]}}]}}]} as unknown as DocumentNode<GetTimerQuery, GetTimerQueryVariables>;
export const MovePointerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"MovePointer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"alpha"}},"type":{"kind":"NamedType","name":{"kind":"Name","value":"Float"}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"beta"}},"type":{"kind":"NamedType","name":{"kind":"Name","value":"Float"}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"gamma"}},"type":{"kind":"NamedType","name":{"kind":"Name","value":"Float"}}},{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"movePointer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"alpha"},"value":{"kind":"Variable","name":{"kind":"Name","value":"alpha"}}},{"kind":"Argument","name":{"kind":"Name","value":"beta"},"value":{"kind":"Variable","name":{"kind":"Name","value":"beta"}}},{"kind":"Argument","name":{"kind":"Name","value":"gamma"},"value":{"kind":"Variable","name":{"kind":"Name","value":"gamma"}}},{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"user"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]}}]} as unknown as DocumentNode<MovePointerMutation, MovePointerMutationVariables>;
export const DisconnectPointerDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"mutation","name":{"kind":"Name","value":"DisconnectPointer"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"disconnectPointer"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"accessToken"},"value":{"kind":"Variable","name":{"kind":"Name","value":"accessToken"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]} as unknown as DocumentNode<DisconnectPointerMutation, DisconnectPointerMutationVariables>;