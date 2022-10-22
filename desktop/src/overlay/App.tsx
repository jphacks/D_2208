import { ipcRenderer } from "electron";
import { useEffect, useState } from "react";

import { Pointer } from "./Pointer";
import type { Coordinate } from "./types";

export const App = () => {
  const [position, setPosition] = useState<Coordinate | null>(null);

  const [showingPointer, setShowingPointer] = useState(false);

  const [cnt, setCnt] = useState(0);

  useEffect(() => {
    ipcRenderer.on(
      "update-pointer-position",
      (_, position: Coordinate | null) => {
        setPosition(position);
        if (position === null) {
          setShowingPointer(false);
          setCnt(0);
        } else {
          setShowingPointer(true);
          setCnt((cnt) => cnt + 1);
        }
      }
    );

    ipcRenderer.on("hide-pointer", () => {
      setShowingPointer(false);
      setCnt(0);
      setPosition(null);
    });
  }, []);

  console.log(position);

  if (position === null) {
    return <>うんこ</>;
  }

  return (
    <Pointer cnt={cnt} position={position} showingPointer={showingPointer} />
  );
};
