export type Coordinate = { x: number; y: number };

export type ElectronApi = {
  onUpdateRotation: (callback: (position: Coordinate) => void) => void;
  hidePointer: (callback: () => void) => void;
};
