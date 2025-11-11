export interface Evento {
  fechaHoraOcurrencia: string;
  latitudEpicentro: number;
  longitudEpicentro: number;
  latitudHipocentro: number;
  longitudHipocentro: number;
  valorMagnitud: number;
}

interface DatosSismicos {
    alcanceSismo: string;
    clasificacionSismo: string;
    origenDeGeneracion: string;
}

interface SeriesTemporalesClasificadas {
    codigoEstacion: number;
    muestrasSismicas: {
      fechaHoraMuestra: string;
      detalles: {
        valor: string;
        tipoDeDato: {
          denominacion: string;
          nombreUnidadMedida: string;
        };
      }[];
    }[];
}

interface CambiosEstado {
    fechaHoraDesde: string;
    fechaHoraHasta: string | null;
    responsableInspeccion: string | null;
    estado: string;
}

export interface DetalleEvento {
  datosSismicos: DatosSismicos;
  seriesTemporales: SeriesTemporalesClasificadas[];
  cambiosDeEstado: CambiosEstado[];
}