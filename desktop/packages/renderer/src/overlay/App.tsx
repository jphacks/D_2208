import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import { useEffect, useState } from "react";

import { SpotlightPointer } from "./SpotlightPointer";

import {
  getPointers,
  onUpdatePointers,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [pointers, setPointers] = useState<UpdatePointersMessage | null>(null);

  useEffect(() => {
    onUpdatePointers(setPointers);

    getPointers().then(setPointers);
  }, []);

  if (pointers === null) {
    return null;
  }

  return <SpotlightPointer pointers={pointers} />;
};
