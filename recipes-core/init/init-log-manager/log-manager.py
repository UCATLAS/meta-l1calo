#Simple python script for managing log rotations on the gFEX Zynq
#For suggestions/problems: simone.sottocornola@cern.ch

import os
import tarfile
import schedule, time

workdir = "/logs/"
maxSize = 1500000000    # Max allowed size of the log folder. If exceeded, triggers a log rotation.
clearFrequency = 30     # Days between log rotation (this happens independently from the /log folder size)

#Find files with a given extension
def find_files(base, ext):
  files=[]
  for file in os.listdir(base):
    if file.endswith(ext):
      files.append(base+file)
  return(files)

#Hack to truncate log files
def empty(log):
  f = open(log, "w")
  f.close()

#Returns folder size
def get_dir_size(path):
  total_size = 0
  for dirpath, dirnames, filenames in os.walk(path):
    for f in filenames:
      fp = os.path.join(dirpath, f)
      # skip if it is symbolic link
      if not os.path.islink(fp):
        total_size += os.path.getsize(fp)
  return total_size

#Job to check for workdir memory: calls Job if mem > maxSize
def mem_check():
  size = get_dir_size(workdir)
  if size > maxSize: 
    job()

#Job function (delete tar, create new tar, truncates logs)
def job():
  logs = find_files(workdir, ".log")
  tarf = find_files(workdir, ".gz")
  #Remove old tar
  for f in tarf:
    os.remove(f)
  #Add logs to tar and truncate 
  for log in logs:
    with tarfile.open("%s.tar.gz" %log,"w:gz") as tar:
      tar.add(log)
    empty(log)

####################    main    ####################
if __name__ == "__main__":
  schedule.every(clearFrequency).days.do(job)
  schedule.every(1).hours.do(mem_check)
  while True:
    schedule.run_pending()
    time.sleep(120)


