import { useWindowSize } from "@react-hook/window-size";

export const App = () => {
  const [width, height] = useWindowSize();

  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      viewBox={`0 0 ${width} ${height}`}
    >
      {<circle cx={20} cy={20} r={10} fill="red" />}
    </svg>
  );
};
