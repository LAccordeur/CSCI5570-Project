from numpy import random
import numpy as np
import datetime

def generate_random_distributed_query_workload(spatial_range, time_range, spatial_width, temporal_width, query_num, output_path):

    """

    :param spatial_range: lon_min, lat_min, lon_max, lat_max
    :param time_range: format like 2010-01-01 00:34:00, 2010-01-30 00:34:00
    :param spatial_width:
    :param temporal_width: unit is hour
    :param query_num:
    :return: format: time_min, time_max, lon_min, lat_min, lon_max, lat_max
    """
    workload_batch = []

    time_start = datetime.datetime.strptime(time_range[0], "%Y-%m-%d %H:%M:%S")
    time_stop = datetime.datetime.strptime(time_range[1], "%Y-%m-%d %H:%M:%S")


    random_time_values = np.around(random.random(query_num),2)
    random_longitude_values = random.random(query_num)
    random_latitude_value = random.random(query_num)

    for i in range(query_num):

        time_min = time_start + (time_stop - time_start) * random_time_values[i]
        time_min = time_min.replace(microsecond=0)
        time_max = time_min + datetime.timedelta(hours=temporal_width) + datetime.timedelta(seconds=-1)

        lon_min = spatial_range[0] + (spatial_range[2] - spatial_range[0]) * random_longitude_values[i]
        lon_max = lon_min + spatial_width

        lat_min = spatial_range[1] + (spatial_range[3] - spatial_range[1]) * random_latitude_value[i]
        lat_max = lat_min + spatial_width

        query_rectangle = "%s,%s,%s,%s,%s,%s,%s\n" % (i, time_min, time_max, np.around(lon_min, 6), np.around(lon_max, 6), np.around(lat_min, 6), np.around(lat_max, 6))
        workload_batch.append(query_rectangle)

        print(query_rectangle)

    with open(output_path, 'w') as output_fd:
        output_fd.writelines(workload_batch)
    print()


def generate_uniform_distributed_query_workload(spatial_range, time_range, spatial_width, temporal_width, query_num, output_path):

    workload_batch = []

    random_points = random.uniform(0, 1, (query_num, 3))

    time_start = datetime.datetime.strptime(time_range[0], "%Y-%m-%d %H:%M:%S")
    time_stop = datetime.datetime.strptime(time_range[1], "%Y-%m-%d %H:%M:%S")

    for i in range(query_num):
        time_min = time_start + (time_stop - time_start) * np.around(random_points[i][0], 2)
        time_min = time_min.replace(microsecond=0)
        time_max = time_min + datetime.timedelta(hours=temporal_width) + datetime.timedelta(seconds=-1)

        lon_min = spatial_range[0] + (spatial_range[2] - spatial_range[0]) * random_points[i][1]
        lon_max = lon_min + spatial_width

        lat_min = spatial_range[1] + (spatial_range[3] - spatial_range[1]) * random_points[i][2]
        lat_max = lat_min + spatial_width

        query_rectangle = "%s,%s,%s,%s,%s,%s,%s\n" % (
        i, time_min, time_max, np.around(lon_min, 6), np.around(lon_max, 6), np.around(lat_min, 6), np.around(lat_max, 6))
        workload_batch.append(query_rectangle)

        print(query_rectangle)

    with open(output_path, 'w') as output_fd:
        output_fd.writelines(workload_batch)
    print()


def generate_zipf_distributed_query_workload(spatial_range, time_range, spatial_width, temporal_width, query_num, output_path):
    workload_batch = []

    random_points = random.uniform(0, 1, (query_num, 3))

    zipf_values = random.zipf(2, query_num)
    print(zipf_values)
    time_start = datetime.datetime.strptime(time_range[0], "%Y-%m-%d %H:%M:%S")
    time_stop = datetime.datetime.strptime(time_range[1], "%Y-%m-%d %H:%M:%S")

    for i in range(query_num):

        index = zipf_values[i] % query_num

        time_min = time_start + (time_stop - time_start) * np.around(random_points[index][0], 2)
        time_min = time_min.replace(microsecond=0)
        time_max = time_min + datetime.timedelta(hours=temporal_width) + datetime.timedelta(seconds=-1)

        lon_min = spatial_range[0] + (spatial_range[2] - spatial_range[0]) * random_points[index][1]
        lon_max = lon_min + spatial_width

        lat_min = spatial_range[1] + (spatial_range[3] - spatial_range[1]) * random_points[index][2]
        lat_max = lat_min + spatial_width

        query_rectangle = "%s,%s,%s,%s,%s,%s,%s\n" % (
        i, time_min, time_max, np.around(lon_min, 6), np.around(lon_max, 6), np.around(lat_min, 6), np.around(lat_max, 6))
        workload_batch.append(query_rectangle)

        print(query_rectangle)

    with open(output_path, 'w') as output_fd:
        output_fd.writelines(workload_batch)

    print()

def get_line_count(filename):
    count = 0
    fd = open(filename)
    while True:
        buffer = fd.read(1024*8192)
        if not buffer:
            break
        count += buffer.count('\n')
    fd.close()
    print("line count: " + str(count))
    return count

def generate_data_distributed_query_workload(drop_off_file, spatial_range, spatial_width, temporal_width, query_num, output_path):

    workload_batch = []

    drop_off_file_fd = open(drop_off_file, 'r')
    count = 0
    query_count = 1
    line = drop_off_file_fd.readline()
    while line:
        count = count + 1

        if (count % 10000 == 0):
            # line format is dropoff_seq_id, medallion, dropoff_time, trip_time_in_secs, trip_distance, dropoff_longitude, dropoff_latitude
            items = line.split(",")
            drop_off_time_str = items[2]
            drop_off_lon = items[5]
            drop_off_lat = items[6]

            drop_off_time = datetime.datetime.strptime(drop_off_time_str, "%Y-%m-%d %H:%M:%S")


            if float(drop_off_lon) > spatial_range[0] and float(drop_off_lon) < spatial_range[2] and float(drop_off_lat) > spatial_range[1] and float(drop_off_lat) < spatial_range[3]:

                rec_time_min = drop_off_time + datetime.timedelta(hours=-(1.0*temporal_width/2))
                rec_time_min_str = datetime.datetime.strftime(rec_time_min, "%Y-%m-%d %H:%M:%S")
                rec_time_max = drop_off_time + datetime.timedelta(hours=(1.0*temporal_width/2)) + datetime.timedelta(seconds=-1)
                rec_time_max_str = datetime.datetime.strftime(rec_time_max, "%Y-%m-%d %H:%M:%S")
                spatial_width_half = 1.0 * spatial_width / 2
                rec_lon_min = float(drop_off_lon) - spatial_width_half
                rec_lon_max = float(drop_off_lon) + spatial_width_half
                rec_lat_min = float(drop_off_lat) - spatial_width_half
                rec_lat_max = float(drop_off_lat) + spatial_width_half
                query_rectangle = "%s,%s,%s,%s,%s,%s,%s\n" % (query_count, rec_time_min_str, rec_time_max_str, np.round(rec_lon_min, 6), np.round(rec_lon_max, 6), np.round(rec_lat_min, 6), np.round(rec_lat_max, 6))
                workload_batch.append(query_rectangle)
                query_count = query_count + 1

        line = drop_off_file_fd.readline()

    print(len(workload_batch))


    sample_marker = int(len(workload_batch) / query_num)
    workload_batch_sample = []
    query_count = 0
    for index, item in enumerate(workload_batch):
        if (index % sample_marker == 0):
            item_splits = item.split(",")
            query_rectangle = "%s,%s,%s,%s,%s,%s,%s" % (query_count, item_splits[1], item_splits[2], item_splits[3], item_splits[4], item_splits[5], item_splits[6])
            workload_batch_sample.append(query_rectangle)
            print(item)
            print(query_rectangle)
            query_count = query_count + 1

    print(len(workload_batch_sample))

    with open(output_path, mode='a') as output_fd:
        output_fd.writelines(workload_batch_sample[0:60])




if __name__ == "__main__":
    
    spatial_list = [0.1, 0.01, 0.001]
    time_list = [1, 24, 24*7]
    query_num = 60
    filename_time_map = {}
    filename_time_map[1] = "1h"
    filename_time_map[24] = "24h"
    filename_time_map[24*7] = "7d"
    print(filename_time_map)
    filename_spatial_map = {}
    filename_spatial_map[0.1] = "1"
    filename_spatial_map[0.01] = "01"
    filename_spatial_map[0.001] = "001"


    for spatial_item in spatial_list:
        for time_item in time_list:
            filename = "synthetic_%s_%s_60_random" % (filename_spatial_map[spatial_item], filename_time_map[time_item])
            generate_random_distributed_query_workload([-74.00, 40.65, -73.85, 40.80], ["2010-01-01 00:00:00", "2010-01-31 23:59:59"], spatial_item, time_item, query_num, filename)
            print(filename)
    
    


    for spatial_item in spatial_list:
        for time_item in time_list:
            filename = "synthetic_%s_%s_60_data" % (filename_spatial_map[spatial_item], filename_time_map[time_item])
            generate_data_distributed_query_workload("src/main/resources/dataset/trip_data_1_pickup.csv", [-74.05, 40.60, -73.75, 40.90], spatial_item, time_item, query_num, filename)
            print(filename)