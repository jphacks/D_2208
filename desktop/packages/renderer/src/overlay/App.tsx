import type { Pointers } from "@smartpointer-desktop/shared";
import { useEffect, useState } from "react";

import { Pointer } from "./Pointer";

import {
  onUpdatePointers,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [position, setPosition] = useState<Pointers>([]);

  useEffect(() => {
    onUpdatePointers(setPosition);
  }, []);

  return <Pointer pointers={position} />;
};
