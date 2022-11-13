import type {
  CustomPointerType,
  PointerType,
  UpdatePointersMessage,
} from "@smartpointer-desktop/shared";
import { useEffect, useState } from "react";

import { ArrowPointer } from "./ArrowPointer";
import { CustomPointer } from "./CustomPointer";
import { FingerPointer } from "./FingerPointer";
import { SpotlightPointer } from "./SpotlightPointer";

import {
  getPointers,
  onUpdatePointers,
  onUpdatePointerType,
  // eslint-disable-next-line import/no-unresolved
} from "#preload";

export const App = () => {
  const [pointers, setPointers] = useState<UpdatePointersMessage | null>(null);
  const [pointerType, setPointerType] = useState<PointerType | null>(null);

  useEffect(() => {
    onUpdatePointers(setPointers);
    onUpdatePointerType(setPointerType);

    getPointers().then(({ pointers, pointerType }) => {
      setPointers(pointers);
      setPointerType(pointerType);
    });
  }, []);

  if (pointers === null || pointerType === null) {
    return null;
  }

  switch (pointerType.id) {
    case "SPOTLIGHT":
      return <SpotlightPointer pointers={pointers} />;
    case "ARROW":
      return <ArrowPointer pointers={pointers} />;
    case "FINGER":
      return <FingerPointer pointers={pointers} />;
    default:
      return (
        <CustomPointer
          pointers={pointers}
          pointerType={pointerType as CustomPointerType}
        />
      );
  }
};
