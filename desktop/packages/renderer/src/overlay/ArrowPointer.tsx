import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import type { FC } from "react";

import { PointerWrapper } from "./PointerWrapper";

export const ArrowPointer: FC<{ pointers: UpdatePointersMessage }> = ({
  pointers,
}) => (
  <PointerWrapper
    pointers={pointers}
    renderPointer={(color) => (
      <svg
        width="44"
        height="58"
        viewBox="0 0 44 58"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        style={{
          width: "auto",
          height: "3em",
        }}
      >
        <path
          d="M1 53.7579V4.24287C1 1.57012 4.23149 0.231635 6.12137 2.12159L41.127 37.1287C43.0168 39.0186 41.6783 42.25 39.0056 42.25H20.9946C20.199 42.25 19.4359 42.566 18.8733 43.1286L6.1212 55.8793C4.23126 57.7691 1 56.4305 1 53.7579Z"
          fill={color}
          stroke="white"
          strokeWidth="2"
        />
      </svg>
    )}
  />
);
