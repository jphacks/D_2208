import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import { useEffect, useState } from "react";

import { Pointer } from "./Pointer";

import {
  onUpdatePointers,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [pointers, setPointers] = useState<UpdatePointersMessage>([]);

  useEffect(() => {
    onUpdatePointers(setPointers);
  }, []);

  return <Pointer pointers={pointers} />;
};
