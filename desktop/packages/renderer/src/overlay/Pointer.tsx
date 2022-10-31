import { useWindowSize } from "@react-hook/window-size";
import type { Pointers } from "@smartpointer-desktop/shared";
import type { FC } from "react";

type Props = {
  pointers: Pointers;
};
export const Pointer: FC<Props> = ({ pointers }) => {
  const [width, height] = useWindowSize();
  const radius = 100;

  if (pointers.length === 0) {
    return null;
  }

  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox={`0 0 ${width} ${height}`}
    >
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d={`
        M 0 0
        h ${width}
        v ${height}
        h ${-width}
        v ${-height}
        Z
        ${pointers
          .map(
            ({ pointer }) => `
          M ${width / 2 + pointer.x * width} ${height / 2 + pointer.y * width}
          a ${radius} ${radius} 0 1 1 1 0
          Z
        `
          )
          .join("")}`}
        fill="rgba(0, 0, 0, 0.5)"
      />
    </svg>
  );
};
