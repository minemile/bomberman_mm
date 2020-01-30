import random
import pandas as pd
import matplotlib.pyplot as plt
import geopandas as gpd
import plotly.express as px
from shapely.geometry import LineString, Point, Polygon
import networkx as nx
import osmnx as ox
import plotly.graph_objects as go


def get_stops(path):
    path_null = path[path['Trip Distance (km)'].isnull()]
    last_index = path_null.index.min()
    dt_index = path_null.index
    stops = []
    for i in range(len(path_null) - 1):
        if dt_index[i] + pd.Timedelta(1, unit='t') != dt_index[i+1]:
            stop_df = path_null[last_index:dt_index[i+1]]
            lat, lon = stop_df[["lat", "lon"]].mean()
            stop_time = stop_df.index.max() - stop_df.index.min()
            stops.append((len(stops), lat, lon, stop_time, last_index))
            last_index = dt_index[i+1]
    stops = pd.DataFrame(stops, columns=["stop_id", "lat", "lon", "stop_time", "last_time_stop"])
    return stops

def get_trip_dates(df):
    # Seems like diffs == null adds trips with ~0 length
    # trip_dates = df_m[(df_m['diffs'] < 0) | df_m['diffs'].isnull()] 
    # trip_dates_with_nulls = trip_dates[trip_dates['Trip Distance (km)'] == 0].index
    trip_dates = df[(df['diffs'] < 0)]
    trip_dates = trip_dates[trip_dates['Trip Distance (km)'] == 0].index
    print(f"Trip dates: {len(trip_dates)}")
    if len(trip_dates) == 0:
        print("Probably df contains one trip")
        return df.index[-1:]
    return trip_dates

def make_trips(df, trip_dates):
    # TODO: remove repitative rows in trips
    df_m_d = df.copy().dropna()
    prev_date = df_m_d.index.min()
    df_m_d['trip_id'] = None
    to_drop_index = []
    for i, trip_date in enumerate(trip_dates):
        trip = df_m_d.loc[prev_date:trip_date]
        if len(trip) < 5:
            print(f"Drop to small trip {i}")
            continue
        if trip.iloc[-2]['diffs'] < 0:
            to_drop_index.append(trip.index[-2])
            
        df_m_d.loc[prev_date:trip_date, "trip_id"] = i
        prev_date = trip_date
        if i == len(trip_dates) - 1:
            df_m_d.loc[trip_date:, "trip_id"] = i + 1

    # Drop invalid rows cuz of negative diff in last row of some trips
    df_m_d.drop(to_drop_index, inplace=True)
    return df_m_d


def plot_path(full_path):
    stops = get_stops(full_path)
    lon_center, lat_center = full_path[['lon', 'lat']].mean()
    geo_map = go.Figure()
    
    path_without_null = full_path.dropna()
    template = "Speed: {0:.2f}<br>Distance: {1:.2f}<br>Date:{2}"
    texts_without_null = [template.format(row['speed'], row['Trip Distance (km)'], j) for j, row in path_without_null.iterrows()]
    
    geo_map.add_trace(go.Scattermapbox(lon=path_without_null['lon'], lat=path_without_null['lat'], text=texts_without_null, mode='lines', name=f"Trip", line=dict(width=5)))
    
    nulls = full_path[full_path["Trip Distance (km)"].isnull()]
    template = "Speed: {0:.2f}<br>Distance: {1:.2f}<br>Date:{2}"
    texts_nulls = [template.format(row['speed'], row['Trip Distance (km)'], j) for j, row in nulls.iterrows()]
    geo_map.add_trace(
        go.Scattermapbox(lon=nulls['lon'], lat=nulls['lat'], text=texts_nulls, mode='markers', name=f"Nulls", marker=dict(size=15, opacity=0.7))
        )
    
    template = "Stop_id: {0}<br>Stop time: {1}<br>Last time stop: {2}"
    texts_stops = [template.format(row['stop_id'], row['stop_time'], row['last_time_stop']) for j, row in stops.iterrows()]
    geo_map.add_trace(go.Scattermapbox(lon=stops['lon'], lat=stops['lat'], text=texts_stops, mode='markers', name=f"Stops", marker=dict(size=15, opacity=0.7)))
    
    geo_map.update_layout(
        margin ={'l':0,'t':0,'b':0,'r':0},
        mapbox = {
            'style': "open-street-map",
            'zoom': 7,
            "bearing": 0,
            "pitch": 0,
            'center': {'lon': lon_center, 'lat': lat_center}
            }
    )
    return geo_map
