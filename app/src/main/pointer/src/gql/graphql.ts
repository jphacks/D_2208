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

/** アクセストークン */
export type AccessToken = {
  __typename?: 'AccessToken';
  /** アクセストークン */
  accessToken: Scalars['String'];
  /** トークンタイプ */
  tokenType: Scalars['String'];
  /** TTL [s] */
  ttl: Scalars['Int'];
};

export type Mutation = {
  __typename?: 'Mutation';
  /** ルーム作成API */
  createRoom: Room;
  /** ルーム削除API */
  deleteRoom: Scalars['ID'];
  /** ポインター切断API */
  disconnectPointer: PointerControl;
  /** スライドを進めるAPI */
  goNextSlide: SlideControl;
  /** スライドを戻すAPI */
  goPreviousSlide: SlideControl;
  /** ルーム入室API */
  joinRoom: AccessToken;
  /** ポインター操作API */
  movePointer: PointerControl;
  /** タイマーリセットAPI */
  resetTimer: Timer;
  /** タイマー再開API */
  resumeTimer: Timer;
  /** タイマー開始API */
  startTimer: Timer;
  /** タイマー停止API */
  stopTimer: Timer;
};


export type MutationDeleteRoomArgs = {
  roomId: Scalars['ID'];
};


export type MutationJoinRoomArgs = {
  passcode: Scalars['String'];
  roomId: Scalars['ID'];
  userName: Scalars['String'];
};


export type MutationStartTimerArgs = {
  inputTime: Scalars['Int'];
};


export type MutationStopTimerArgs = {
  remainingTimeAtPaused: Scalars['Int'];
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
  /** タイマー取得API */
  getTimer: Timer;
  /** ユーザリスト取得API */
  getUsers: Users;
  /** ヘルスチェックAPI */
  health: Scalars['Boolean'];
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
  /** ポインター操作購読API */
  subscribeToPointer: PointerControl;
  /** ポインター切断イベント購読API */
  subscribeToPointerDisconnectEvent: User;
  /** スライド操作購読API */
  subscribeToSlideControl: SlideControl;
  /** タイマー購読API */
  subscribeToTimer: Timer;
  /** ユーザリスト購読API */
  subscribeToUsers: Array<User>;
};


export type SubscriptionSubscribeToPointerArgs = {
  roomId: Scalars['ID'];
};


export type SubscriptionSubscribeToPointerDisconnectEventArgs = {
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
