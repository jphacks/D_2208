import type { PointerCoordinate } from "@smartpointer-desktop/shared";
import { useEffect, useState } from "react";

import { Pointer } from "./Pointer";

import {
  onHidePointer,
  onUpdatePointerPosition,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [position, setPosition] = useState<PointerCoordinate | null>(null);

  useEffect(() => {
    onUpdatePointerPosition((position) => {
      setPosition(position);
    });

    onHidePointer(() => {
      setPosition(null);
    });
  }, []);

  console.log(position);

  if (position === null) {
    return null;
  }

  return <Pointer position={position} />;
};
