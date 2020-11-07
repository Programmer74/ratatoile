import { RowData, Scheme } from "../tableForData/types";
import { Button } from "@material-ui/core";
import React from "react";

const forAcceptPart = (onDelete: ()=>void, onAccept: ()=>void) => {
  return {
    tableType: {
      label: "Тип столика",
    },
    seats: {
      label: "Число гостей",
    },
    controls: {
      label: "Управление",
      renderer: (value: RowData) => {
        return (
            <div className="controls">
              <div className="btnWrapper">
                <Button
                    variant="outlined"
                    color="secondary"
                    onClick={onDelete}
                    value={value.id}
                >
                  Удалить
                </Button>
              </div>
              <div className="btnWrapper">
                <Button
                    variant="contained"
                    color="primary"
                    onClick={onAccept}
                    value={value.id}
                >
                  Подтвердить
                </Button>
              </div>
            </div>
        );
      },
    },
  };
}

const forAllPart = (onDelete: ()=>void) => {
  return {
    controls: {
      label: "Управление",
      renderer: (value: RowData) => {
        return (
            <div className="controls">
              <div className="btnWrapper">
                <Button
                    variant="outlined"
                    color="secondary"
                    onClick={onDelete}
                    value={value.id}
                >
                  Удалить
                </Button>
              </div>
            </div>
        );
      },
    },
  };
}

export const getScheme = (props: any): Scheme => {
  const all = {
    name: {
      label: "Фамилия, Имя",
    },
    phone: {
      label: "Номер телефона",
    },
    tableCandidateId: {
      label: props.forAccept ? "Предварительный столик" : 'Назначенный столик',
    },
    time: {
      label: "Время бронирования",
      formatter: (time: { from: number; to: number }) => {
        return `${new Date(time.from).toLocaleDateString()}, ${new Date(
          time.from
        ).toLocaleTimeString()} - ${new Date(time.to).toLocaleTimeString()}`;
      },
    },
  };

  return props.forAccept ? {...all, ...forAcceptPart(props.onDelete, props.onAccept)} : {...all, ...forAllPart(props.onDelete)};
};
