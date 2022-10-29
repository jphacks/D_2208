import { useEffect, useState } from "react";

import { Pointer } from "./Pointer";

import {
  Coordinate,
  onHidePointer,
  onUpdatePointerPosition,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [position, setPosition] = useState<Coordinate | null>(null);

  const [showingPointer, setShowingPointer] = useState(false);

  const [cnt, setCnt] = useState(0);

  useEffect(() => {
    onUpdatePointerPosition((position) => {
      setPosition(position);
      if (position === null) {
        setShowingPointer(false);
        setCnt(0);
      } else {
        setShowingPointer(true);
        setCnt((cnt) => cnt + 1);
      }
    });

    onHidePointer(() => {
      setShowingPointer(false);
      setCnt(0);
      setPosition(null);
    });
  }, []);

  console.log(position);

  if (position === null) {
    return null;
  }

  return (
    <Pointer cnt={cnt} position={position} showingPointer={showingPointer} />
  );
};
