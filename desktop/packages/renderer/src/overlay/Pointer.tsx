import { useWindowSize } from "@react-hook/window-size";
import type { PointerCoordinate } from "@smartpointer-desktop/shared";
import type { FC } from "react";

type Props = {
  position: PointerCoordinate;
};
export const Pointer: FC<Props> = ({ position }) => {
  const [width, height] = useWindowSize();
  const radius = 100;

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

        M ${width / 2 + position.x * width} ${height / 2 + position.y * width}
        a ${radius} ${radius} 0 1 1 1 0
        Z
        `}
        fill="rgba(0, 0, 0, 0.5)"
      />
    </svg>
  );
};
