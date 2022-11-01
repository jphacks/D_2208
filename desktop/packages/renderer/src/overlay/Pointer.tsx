import { useWindowSize } from "@react-hook/window-size";
import type { Pointers } from "@smartpointer-desktop/shared";
import type { FC } from "react";

type Props = {
  pointers: Pointers;
};
export const Pointer: FC<Props> = ({ pointers }) => {
  const [width, height] = useWindowSize();
  const radiusRate = 0.1;

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
      <defs>
        <mask id="mask">
          <rect x="0" y="0" width={width} height={height} fill="white" />
          {pointers.map(({ pointer, userId }) => (
            <circle
              key={userId}
              cx={width / 2 + pointer.x * width}
              cy={height / 2 + pointer.y * height}
              r={Math.min(width, height) * radiusRate}
              fill="rgb(0 0 0 / 70%)"
            />
          ))}
        </mask>
      </defs>
      <rect
        x="0"
        y="0"
        width={width}
        height={height}
        fill="rgb(0 0 0 / 70%)"
        mask="url(#mask)"
      />
    </svg>
  );
};
