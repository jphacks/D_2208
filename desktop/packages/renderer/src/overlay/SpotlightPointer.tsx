import { useWindowSize } from "@react-hook/window-size";
import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import type { FC } from "react";

type Props = {
  pointers: UpdatePointersMessage;
};
export const SpotlightPointer: FC<Props> = ({ pointers }) => {
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
          {pointers.map(({ coordinate, user }) => (
            <circle
              key={user.id}
              cx={width / 2 + coordinate.x * width}
              cy={height / 2 + coordinate.y * height}
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
