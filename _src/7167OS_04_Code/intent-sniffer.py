from drozer.modules import Module,common
from drozer.modules import android
class Intents(Module, common.PackageManager):
  name = “Dump recent intents to the console”
  description = “This module allows you to see the most recent intents that were sent, via the ActivityManager”
  examples = “run ex.sniffer.intents”
  author = “[your name]”
  date = “[the date]”
  license = “GNU GPL”
  path = [“ex”,”sniffer”]
  def execute(self,arguments):
    self.stdout.write(“[*] initializing intent sniffer…\n”)
    context = self.getContext()
    activityService = context.getSystemService(“activity”)
    self.stdout.write(“[*] got system service ..\n”)
    recentTasks = activityService.getRecentTasks(1000,1)
    self.stdout.write(“[*] recentTasts Extracted..\n”)
    list_length = recentTasks.size()
    self.stdout.write(“[*] Extracted %s tasks ..\n” % (list_length))
   for task in range(list_length):
      cur_task = recentTasks.get(task)
      cur_taskBaseIntent = cur_task.baseIntent
      self.stdout.write(“\t[%d] %s\n” % (task,cur_taskBaseIntent.toString()))

