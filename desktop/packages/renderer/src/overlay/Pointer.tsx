import { useWindowSize } from "@react-hook/window-size";
import type { PointerCoordinate } from "@smartpointer-desktop/shared";
import type { FC } from "react";

type Props = {
  position: PointerCoordinate;
  showingPointer: boolean;
  cnt: number;
};
export const Pointer: FC<Props> = ({ position, showingPointer, cnt }) => {
  const [width, height] = useWindowSize();
  const radius = 100;

  console.log(showingPointer);

  // FIXME: クロップしたエリアが暗くなる
  // TODO: 残像が残るので、初期描画位置を画面外に設定してごまかしてるので修正する
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

        M ${(cnt > 5 ? width / 2 : 0) + position.x * width} ${
          (cnt > 5 ? height / 2 : 0) + position.y * width
        }
        a ${radius} ${radius} 0 1 1 1 0
        Z
        `}
        fill="rgba(0, 0, 0, 0.5)"
      />
    </svg>
  );
};
